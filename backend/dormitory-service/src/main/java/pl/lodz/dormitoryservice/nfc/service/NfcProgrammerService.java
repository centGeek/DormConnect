package pl.lodz.dormitoryservice.nfc.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import pl.lodz.dormitoryservice.nfc.dto.*;
import pl.lodz.dormitoryservice.entity.NfcProgrammerEntity;
import pl.lodz.dormitoryservice.nfc.exception.DeviceConnectionException;
import pl.lodz.dormitoryservice.nfc.exception.DeviceNotFoundException;
import pl.lodz.dormitoryservice.nfc.mapper.NfcProgrammerMapper;
import pl.lodz.dormitoryservice.repository.NfcProgrammerRepository;

@Service
public class NfcProgrammerService {
    private final NfcProgrammerRepository nfcProgrammerRepository;

    @Autowired
    private RestTemplate restTemplate;

    public NfcProgrammerService(
            NfcProgrammerRepository nfcProgrammerRepository) {
        this.nfcProgrammerRepository = nfcProgrammerRepository;
    }

    public RegisterNfcProgrammerDTO registerNfcProgrammer(RegisterNfcProgrammerDTO registerNfcProgrammerDTO) {
        if (registerNfcProgrammerDTO == null) {
            throw new IllegalArgumentException("Invalid input: registerNfcProgrammerDTO cannot be null");
        }
        // if the programmer already exisits ind the db, update its information
        NfcProgrammerEntity existingProgrammer = nfcProgrammerRepository.findByUuid(registerNfcProgrammerDTO.uuid());
        if (existingProgrammer != null) {
            existingProgrammer.setIpAddress(registerNfcProgrammerDTO.ipAddress());
            existingProgrammer.setPort(registerNfcProgrammerDTO.port());
            existingProgrammer.setDeviceStatus(registerNfcProgrammerDTO.deviceStatus());
            nfcProgrammerRepository.save(existingProgrammer);
            return NfcProgrammerMapper.toRegisterNfcProgrammerDTO(existingProgrammer);

        }

        // if not, create completely new entity
        NfcProgrammerEntity nfcProgrammerEntity = new NfcProgrammerEntity();
        nfcProgrammerEntity.setUuid(registerNfcProgrammerDTO.uuid());
        nfcProgrammerEntity.setIpAddress(registerNfcProgrammerDTO.ipAddress());
        nfcProgrammerEntity.setPort(registerNfcProgrammerDTO.port());
        nfcProgrammerEntity.setDeviceStatus("ACTIVE");
        NfcProgrammerEntity savedEntity = nfcProgrammerRepository.save(nfcProgrammerEntity);
        return NfcProgrammerMapper.toRegisterNfcProgrammerDTO(savedEntity);
    }

    @Transactional
    public ProgrammedCardDTO programCard(NfcProgramCardDTO entity, String authorizationHeader) {

        NfcProgrammerEntity nfcProgrammer = nfcProgrammerRepository.findByUuid(UUID.fromString(entity.deviceUuid()));


        if (nfcProgrammer == null) {
            throw new DeviceNotFoundException("Device with UUID " + entity.deviceUuid() + " not found");
        }
        try {

            String url = "http://" + nfcProgrammer.getIpAddress() + ":" + Integer.toString(nfcProgrammer.getPort()) + "/api/program-card";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                    "{\"deviceUuid\":\"" + entity.deviceUuid() + "\", \"userUuid\":\"" + entity.userUuid() + "\"}"))
                // .POST(HttpRequest.BodyPublishers.ofString(entity.toString()))
                .build();
            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();
            ProgrammedCardDTO programmedCardDTO = Jackson2ObjectMapperBuilder.json()
                .build()
                .readValue(resp.body(), ProgrammedCardDTO.class);
            
            GetUserDTO currUser = getUserByUuid(entity.userUuid(), authorizationHeader);
            if (currUser == null) {
                throw new DeviceConnectionException("User with UUID " + entity.userUuid() + " not found");
            }

            GetUserDTO updatedUser = new GetUserDTO(
                currUser.id(),
                currUser.uuid(),
                currUser.userName(),
                currUser.email(),
                currUser.role(),
                currUser.isActive(),
                programmedCardDTO.cardUuid()
            );

            System.out.println("Response status code: " + resp.statusCode());
            System.out.println("Response body: " + resp.body());

            GetUserDTO updatedUserEntity = updateUser(updatedUser, authorizationHeader);
            if (updatedUserEntity == null) {
                throw new DeviceConnectionException("Failed to update user with UUID " + currUser.uuid());
            }

            System.out.println("everything ok, user updated");
            return programmedCardDTO;

        } catch (Exception  e) {
            throw new DeviceConnectionException(e.getMessage());
        }

    }

    private GetUserDTO getUserByUuid(String uuid, String authorizationHeader) {
        String url = "http://localhost:8000/api/users/get/by-uuid/" + uuid;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GetUserDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, GetUserDTO.class);
        GetUserDTO userEntity = response.getBody();
        return userEntity;
    }

    private GetUserDTO updateUser(GetUserDTO user, String authorizationHeader) {
        String url = "http://localhost:8091/api/users/update-user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<GetUserDTO> request = new HttpEntity<>( user, headers);
        GetUserDTO updatedUser = restTemplate.postForObject(url, request, GetUserDTO.class);
        if (updatedUser == null) {
            throw new DeviceConnectionException("Failed to update user with UUID " + user.uuid());
        }
        return updatedUser;
   }

    public List<NfcProgrammerDTO> getAllNfcProgrammers() {
        return nfcProgrammerRepository.findAll().stream()
                .map(NfcProgrammerMapper::entityToNfcProgrammerDTO)
                .toList();
    }

    public boolean checkConnectionstatus(String uuid) {
        NfcProgrammerEntity currentDevice = nfcProgrammerRepository.findByUuid(UUID.fromString(uuid));
        String url = "http://" + currentDevice.getIpAddress() + ":" + currentDevice.getPort() + "/api/wifi-info" ;
        NfcStatusDTO statusDto = restTemplate.getForObject(url, NfcStatusDTO.class);
        if (statusDto == null) {
            throw new DeviceConnectionException("Device with UUID " + uuid + " is not reachable");
        }
        if (statusDto.status().equalsIgnoreCase("ok")) {
            return true;
        }
        return false;
    }
}

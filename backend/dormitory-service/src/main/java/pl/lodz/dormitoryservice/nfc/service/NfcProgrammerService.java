package pl.lodz.dormitoryservice.nfc.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

import javax.print.attribute.standard.Media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import pl.lodz.dormitoryservice.nfc.dto.GetUserDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormitoryservice.nfc.dto.ProgrammedCardDTO;
import pl.lodz.dormitoryservice.nfc.dto.RegisterNfcProgrammerDTO;
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

        NfcProgrammerEntity nfcProgrammer = nfcProgrammerRepository.findByUuid(entity.deviceUuid());
        if (nfcProgrammer == null) {
            throw new DeviceNotFoundException("Device with UUID " + entity.deviceUuid() + " not found");
        }
        try {
            String url = "http://" + nfcProgrammer.getIpAddress() + ":" + Integer.toString(nfcProgrammer.getPort()) + "/api/program-card";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);// or whatever curl uses
            headers.set("Connection", "keep-alive");
            headers.set("Accept", "*/*");

            HttpEntity<NfcProgramCardDTO> requestEntity = new HttpEntity<>(entity, headers);

            ProgrammedCardDTO response = restTemplate.postForObject(
                url,
                requestEntity,
                ProgrammedCardDTO.class
            );

            // HttpClient client = HttpClient.newHttpClient();
            // HttpRequest request = HttpRequest.newBuilder()
            //         .uri(java.net.URI.create(url))
            //         .header("Content-Type", "application/json")
            //         .header("Accept", "application/json")
            //         .header("Connection", "keep-alive")
            //         .POST(HttpRequest.BodyPublishers.ofString(
            //             "{\"userUuid\":\"" + entity.userUuid() + "\"}"))
            //         .build();

            // HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println("Response status code: " + response.statusCode());
            System.out.println("Response: " + response );



            if (response == null) {
                throw new DeviceConnectionException("No response from NFC device at " + nfcProgrammer.getIpAddress()
                        + ":" + nfcProgrammer.getPort());
            }

            // GetUserDTO currentUser = getUserByUuid(response.userUuid());
            // if (currentUser == null) {
            //     throw new DeviceConnectionException("User with UUID " + entity.userUuid() + " not found");
            // } 
            // GetUserDTO userWithCard = new GetUserDTO(
            //         currentUser.id(),
            //         currentUser.uuid(),
            //         currentUser.userName(),
            //         currentUser.email(),
            //         currentUser.role(),
            //         currentUser.isActive(),
            //         response.cardUuid()  // Update the card UUID with the one from the response
            // );

            
            // GetUserDTO updatedUser = this.updateUser(userWithCard);


            return response;

        } catch (RestClientException e) {
            throw new DeviceConnectionException(e.getMessage());
        }

    }

    private GetUserDTO getUserByUuid(String uuid) {
        String url = "http://localhost:8000/api/users/get/by-uuid/" + uuid;
        GetUserDTO userEntity = restTemplate.getForObject(url, GetUserDTO.class);
        return userEntity;
    }

    private GetUserDTO updateUser(GetUserDTO user) {
        String url = "http://localhost:8091/api/users/update-user";
        GetUserDTO updatedUser = restTemplate.postForObject(url, user, GetUserDTO.class);
        if (updatedUser == null) {
            throw new DeviceConnectionException("Failed to update user with UUID " + user.uuid());
        }
        return updatedUser;
   }
}

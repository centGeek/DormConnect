package pl.lodz.dormConnect.nfc.service;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.lodz.commons.entity.NfcProgrammerEntity;
import pl.lodz.commons.repository.jpa.NfcProgrammerRepository;
import pl.lodz.dormConnect.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormConnect.nfc.dto.RegisterNfcProgrammerDTO;
import pl.lodz.dormConnect.nfc.exception.DeviceConnectionException;
import pl.lodz.dormConnect.nfc.exception.DeviceNotFoundException;
import pl.lodz.dormConnect.nfc.mapper.NfcProgrammerMapper;

@Service
public class NfcProgrammerService {
    private final NfcProgrammerRepository nfcProgrammerRepository;
   
    @Autowired
    private RestTemplate restTemplate;

    public NfcProgrammerService(
        NfcProgrammerRepository  nfcProgrammerRepository) {
        this.nfcProgrammerRepository = nfcProgrammerRepository;
    }


    public RegisterNfcProgrammerDTO registerNfcProgrammer(RegisterNfcProgrammerDTO registerNfcProgrammerDTO) {
        if (registerNfcProgrammerDTO == null) {
            throw new IllegalArgumentException("Invalid input: registerNfcProgrammerDTO cannot be null");
        }
        NfcProgrammerEntity nfcProgrammerEntity = new NfcProgrammerEntity();
        nfcProgrammerEntity.setUuid(registerNfcProgrammerDTO.uuid());
        nfcProgrammerEntity.setIpAddress(registerNfcProgrammerDTO.ipAddress());
        nfcProgrammerEntity.setPort(registerNfcProgrammerDTO.port());
        nfcProgrammerEntity.setDeviceStatus("ACTIVE");
        NfcProgrammerEntity savedEntity = nfcProgrammerRepository.save(nfcProgrammerEntity);
        return NfcProgrammerMapper.toRegisterNfcProgrammerDTO(savedEntity);
    }


    public NfcProgramCardDTO programCard(NfcProgramCardDTO entity) {

        NfcProgrammerEntity nfcProgrammer = nfcProgrammerRepository.findByUuid(entity.deviceUuid());
        if (nfcProgrammer == null) {
            throw new DeviceNotFoundException("Device with UUID " + entity.deviceUuid() + " not found");
        } 
        try {
            String url = nfcProgrammer.getIpAddress() + Integer.toString(nfcProgrammer.getPort());
            NfcProgramCardDTO response = restTemplate.postForObject(
                url,
                entity,
                NfcProgramCardDTO.class
            );
            if (response == null) {
                throw new DeviceConnectionException("No response from NFC device at " + nfcProgrammer.getIpAddress() + ":" + nfcProgrammer.getPort());
            }
            return response;
        } catch (RestClientException e) {
            throw new DeviceConnectionException("Failed to connect to NFC device at " + nfcProgrammer.getIpAddress() + ":" + nfcProgrammer.getPort(), e);
        }
        
    }
}

package pl.lodz.dormConnect.nfc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.lodz.commons.entity.NfcProgrammerEntity;
import pl.lodz.commons.repository.jpa.NfcProgrammerRepository;
import pl.lodz.dormConnect.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormConnect.nfc.exception.DeviceConnectionException;
import pl.lodz.dormConnect.nfc.exception.DeviceNotFoundException;

@Service
public class NfcProgrammerService {
    private final NfcProgrammerRepository nfcProgrammerRepository;
    private final RestTemplate restTemplate;

    public NfcProgrammerService(
        NfcProgrammerRepository  nfcProgrammerRepository,
        RestTemplate restTemplate) {

        this.nfcProgrammerRepository = nfcProgrammerRepository;
        this.restTemplate = restTemplate;
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

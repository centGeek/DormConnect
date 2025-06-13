//package pl.lodz.dormitoryservice.nfc.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import pl.lodz.commons.entity.NfcProgrammerEntity;
//import pl.lodz.commons.entity.UserEntity;
//import pl.lodz.commons.repository.jpa.NfcProgrammerRepository;
//import pl.lodz.commons.repository.jpa.UserRepository;
//import pl.lodz.dormConnect.nfc.dto.NfcProgramCardDTO;
//import pl.lodz.dormConnect.nfc.dto.ProgrammedCardDTO;
//import pl.lodz.dormConnect.nfc.dto.RegisterNfcProgrammerDTO;
//import pl.lodz.dormConnect.nfc.exception.DeviceConnectionException;
//import pl.lodz.dormConnect.nfc.exception.DeviceNotFoundException;
//import pl.lodz.dormConnect.nfc.mapper.NfcProgrammerMapper;
//
//@Service
//public class NfcProgrammerService {
//    private final NfcProgrammerRepository nfcProgrammerRepository;
//
//    @Autowired
//    private RestTemplate restTemplate;
//    private final UserRepository userRepository;
//
//    public NfcProgrammerService(
//        NfcProgrammerRepository  nfcProgrammerRepository,
//        UserRepository userRepository) {
//        this.nfcProgrammerRepository = nfcProgrammerRepository;
//        this.userRepository = userRepository;
//    }
//
//
//    public RegisterNfcProgrammerDTO registerNfcProgrammer(RegisterNfcProgrammerDTO registerNfcProgrammerDTO) {
//        if (registerNfcProgrammerDTO == null) {
//            throw new IllegalArgumentException("Invalid input: registerNfcProgrammerDTO cannot be null");
//        }
//        // if the programmer already exisits ind the db, update its information
//        NfcProgrammerEntity existingProgrammer = nfcProgrammerRepository.findByUuid(registerNfcProgrammerDTO.uuid());
//        if (existingProgrammer != null) {
//            existingProgrammer.setIpAddress(registerNfcProgrammerDTO.ipAddress());
//            existingProgrammer.setPort(registerNfcProgrammerDTO.port());
//            existingProgrammer.setDeviceStatus(registerNfcProgrammerDTO.deviceStatus());
//            nfcProgrammerRepository.save(existingProgrammer);
//            return NfcProgrammerMapper.toRegisterNfcProgrammerDTO(existingProgrammer);
//
//        }
//
//        // if not, create completely new entity
//        NfcProgrammerEntity nfcProgrammerEntity = new NfcProgrammerEntity();
//        nfcProgrammerEntity.setUuid(registerNfcProgrammerDTO.uuid());
//        nfcProgrammerEntity.setIpAddress(registerNfcProgrammerDTO.ipAddress());
//        nfcProgrammerEntity.setPort(registerNfcProgrammerDTO.port());
//        nfcProgrammerEntity.setDeviceStatus("ACTIVE");
//        NfcProgrammerEntity savedEntity = nfcProgrammerRepository.save(nfcProgrammerEntity);
//        return NfcProgrammerMapper.toRegisterNfcProgrammerDTO(savedEntity);
//    }
//
//
//    @Transactional
//    public ProgrammedCardDTO programCard(NfcProgramCardDTO entity) {
//
//        NfcProgrammerEntity nfcProgrammer = nfcProgrammerRepository.findByUuid(entity.deviceUuid());
//        if (nfcProgrammer == null) {
//            throw new DeviceNotFoundException("Device with UUID " + entity.deviceUuid() + " not found");
//        }
//        try {
//            String url = nfcProgrammer.getIpAddress() + Integer.toString(nfcProgrammer.getPort());
//            HttpEntity<NfcProgramCardDTO> httpEntity = new HttpEntity<>(entity);
//
//            ProgrammedCardDTO response = restTemplate.postForObject(
//                url,
//                httpEntity,
//                ProgrammedCardDTO.class);
//
//
//            if (response == null) {
//                throw new DeviceConnectionException("No response from NFC device at " + nfcProgrammer.getIpAddress() + ":" + nfcProgrammer.getPort());
//            }
//
//            UserEntity currentUser = userRepository.findByUuid(entity.userUuid()).get();
//            if (currentUser == null) {
//                throw new DeviceConnectionException("User with UUID " + entity.userUuid() + " not found");
//            }
//
//            currentUser.setCardUuid(response.cardUuid());
//            userRepository.save(currentUser);
//            return response;
//
//
//        } catch (RestClientException e) {
//            throw new DeviceConnectionException("Failed to connect to NFC device at " + nfcProgrammer.getIpAddress() + ":" + nfcProgrammer.getPort(), e);
//        }
//
//    }
//}

package pl.lodz.dormitoryservice.nfc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import pl.lodz.dormitoryservice.entity.NfcProgrammerEntity;
import pl.lodz.dormitoryservice.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormitoryservice.nfc.dto.ProgrammedCardDTO;
import pl.lodz.dormitoryservice.nfc.dto.RegisterNfcProgrammerDTO;
import pl.lodz.dormitoryservice.nfc.exception.DeviceConnectionException;
import pl.lodz.dormitoryservice.nfc.exception.DeviceNotFoundException;
import pl.lodz.dormitoryservice.nfc.mapper.NfcProgrammerMapper;
import pl.lodz.dormitoryservice.repository.NfcProgrammerRepository;



@Service
public class NfcProgrammerService {
   private final NfcProgrammerRepository nfcProgrammerRepository;

   @Autowired
   private RestTemplate restTemplate;
   private final UserRepository userRepository;

   public NfcProgrammerService(
       NfcProgrammerRepository  nfcProgrammerRepository,
       UserRepository userRepository) {
       this.nfcProgrammerRepository = nfcProgrammerRepository;
       this.userRepository = userRepository;
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
   public ProgrammedCardDTO programCard(NfcProgramCardDTO entity) {

       NfcProgrammerEntity nfcProgrammer = nfcProgrammerRepository.findByUuid(entity.deviceUuid());
       if (nfcProgrammer == null) {
           throw new DeviceNotFoundException("Device with UUID " + entity.deviceUuid() + " not found");
       }
       try {
           String url = nfcProgrammer.getIpAddress() + Integer.toString(nfcProgrammer.getPort());
           HttpEntity<NfcProgramCardDTO> httpEntity = new HttpEntity<>(entity);

           ProgrammedCardDTO response = restTemplate.postForObject(
               url,
               httpEntity,
               ProgrammedCardDTO.class);


           if (response == null) {
               throw new DeviceConnectionException("No response from NFC device at " + nfcProgrammer.getIpAddress() + ":" + nfcProgrammer.getPort());
           }

           UserEntity currentUser = userRepository.findByUuid(entity.userUuid()).get();
           if (currentUser == null) {
               throw new DeviceConnectionException("User with UUID " + entity.userUuid() + " not found");
           }

           currentUser.setCardUuid(response.cardUuid());
           userRepository.save(currentUser);
           return response;


       } catch (RestClientException e) {
           throw new DeviceConnectionException("Failed to connect to NFC device at " + nfcProgrammer.getIpAddress() + ":" + nfcProgrammer.getPort(), e);
       }

   }
}

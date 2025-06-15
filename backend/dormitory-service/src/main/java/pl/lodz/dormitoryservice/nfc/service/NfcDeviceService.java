package pl.lodz.dormitoryservice.nfc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.lodz.dormitoryservice.entity.NfcDeviceEntity;
import pl.lodz.dormitoryservice.nfc.dto.GetNfcDeviceDTO;
import pl.lodz.dormitoryservice.nfc.dto.GetUserDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcAccessRequestDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcDeviceRegisterDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcDeviceUpdateDTO;
import pl.lodz.dormitoryservice.nfc.exception.DeviceNotFoundException;
import pl.lodz.dormitoryservice.nfc.mapper.NfcDeviceMapper;
import pl.lodz.dormitoryservice.repository.CommonRoomAssignmentRepository;
import pl.lodz.dormitoryservice.repository.CommonRoomRepository;
import pl.lodz.dormitoryservice.repository.NfcDeviceRepository;
import pl.lodz.dormitoryservice.repository.RoomAssignmentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class NfcDeviceService {

   private final NfcDeviceRepository nfcDeviceRepository;
   private final RoomAssignmentRepository roomAssignmentRepository;
   private final CommonRoomRepository commonRoomRepository;
   private final CommonRoomAssignmentRepository commonRoomAssignmentRepository;

   @Autowired
   private final RestTemplate restTemplate;


   @Autowired
   public NfcDeviceService(NfcDeviceRepository nfcDeviceRepository,
                           RoomAssignmentRepository roomAssignmentRepository,
                           CommonRoomAssignmentRepository commonRoomAssignmentRepository,
                           CommonRoomRepository commonRoomRepository,
                           RestTemplate restTemplate) {
       this.nfcDeviceRepository = nfcDeviceRepository;
       this.roomAssignmentRepository = roomAssignmentRepository;
       this.commonRoomAssignmentRepository = commonRoomAssignmentRepository;
       this.commonRoomRepository = commonRoomRepository;
       this.restTemplate = restTemplate;    
   }

   public NfcDeviceRegisterDTO registerDevice(NfcDeviceRegisterDTO nfcDeviceDTO) {
       NfcDeviceEntity existingDevice = nfcDeviceRepository.findByUuid(UUID.fromString(nfcDeviceDTO.uuid()));

       // if the device alaready exisits, update it's information and continue
       if (existingDevice != null) {
           existingDevice.setDeviceStatus(nfcDeviceDTO.lockStatus());
           existingDevice.setRoomNumber(nfcDeviceDTO.roomNumber());
           existingDevice.setIpAddress(nfcDeviceDTO.ipAddress());
           // mac address is constant - it never changes
           nfcDeviceRepository.save(existingDevice);
           return NfcDeviceMapper.entityToRegisterDto(existingDevice);
       }

       // else, create a new device and register it
       NfcDeviceEntity device = NfcDeviceMapper.registerDtoToEntity(nfcDeviceDTO);
       device.setId(0);
       device.setDeviceStatus("ACTIVE");
       NfcDeviceEntity saved = nfcDeviceRepository.save(device);
       return NfcDeviceMapper.entityToRegisterDto(saved);
   }

   public boolean checkAccess(NfcAccessRequestDTO nfcAccessRequestDTO) {

       GetUserDTO currentUser = this.getUserByUuid(nfcAccessRequestDTO.user_uuid());
       if (currentUser == null) {
           throw new RuntimeException("Validation failed");
       }

       // check if the user card uuid and the one from the request match
       if (currentUser.() != nfcAccessRequestDTO.card_uid()) {
           throw new UserException("User with uuid: " + nfcAccessRequestDTO.user_uuid()
           + " tried to authenticate with wrong card number: " + nfcAccessRequestDTO.card_uid());
       }

       return roomAssignmentRepository.existsAssignmentAtDate(
               currentUser.getId(),
               LocalDate.now(),
               nfcAccessRequestDTO.roomNumber());
   }

   public NfcDeviceUpdateDTO updateDeviceStatus(NfcDeviceUpdateDTO deviceUpdateDTO) {
       NfcDeviceEntity nfcDevice = nfcDeviceRepository.findByUuid(deviceUpdateDTO.device_uuid());
       if (nfcDevice != null) {
           nfcDevice.setRoomNumber(deviceUpdateDTO.roomNumber());
           nfcDevice.setLockStatus(deviceUpdateDTO.lockStatus());
           nfcDeviceRepository.save(nfcDevice);
           return NfcDeviceMapper.entityToDeviceUpdateDTO(nfcDevice);
       } else {
           throw new DeviceNotFoundException("Device with uuid: " + deviceUpdateDTO.device_uuid() + " not found");
       }
   }


   // still does not work, model need refactoting
   public boolean checkCommonRoomAccess(NfcAccessRequestDTO nfcAccessRequestDTO) {
       // TODO Auto-generated method stub
       GetUserDTO currentUser = getUserByUuid(nfcAccessRequestDTO.user_uuid()); 
       if (currentUser == null) {
           throw new UserException("Error while fetching user with uuid: " + nfcAccessRequestDTO.user_uuid());
       }
       //long currentRoomId = commonRoomRepository.findCommonRoomByName()
       // TODO: Implement logic to fetch the current room ID based on the request
       // CommonRoomAssignment currentAssignment = commonRoomAssignmentRepository
       //         .findCurrentAssingmentByCommonRoomId(nfc)

       throw new UnsupportedOperationException("Unimplemented method 'checkCommonRoomAccess'");
   }

   public List<GetNfcDeviceDTO> getNfcDevices() {
       List<GetNfcDeviceDTO> nfcDevices = nfcDeviceRepository.findAll()
               .stream()
               .map(NfcDeviceMapper::entityToGetDTO)
               .toList();
       if (nfcDevices == null) {
           throw new DeviceNotFoundException("Error while fetching NFC devices");
       }
       return nfcDevices;
   }

   private GetUserDTO getUserByUuid(String uuid) {
        String url = "http://localhost:8091/api/users/get/by-uuid/" + uuid;
        GetUserDTO userEntity = restTemplate.getForObject(url, GetUserDTO.class);
        return userEntity;
   }

}

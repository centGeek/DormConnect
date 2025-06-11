package pl.lodz.dormConnect.nfc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.lodz.commons.entity.NfcDeviceEntity;
import pl.lodz.commons.entity.UserEntity;
import pl.lodz.commons.repository.jpa.CommonRoomAssignmentRepository;
import pl.lodz.commons.repository.jpa.CommonRoomRepository;
import pl.lodz.commons.repository.jpa.NfcDeviceRepository;
import pl.lodz.commons.repository.jpa.RoomAssignmentRepository;
import pl.lodz.commons.repository.jpa.UserRepository;
import pl.lodz.dormConnect.nfc.dto.NfcAccessRequestDTO;
import pl.lodz.dormConnect.nfc.dto.NfcDeviceRegisterDTO;
import pl.lodz.dormConnect.nfc.dto.NfcDeviceUpdateDTO;
import pl.lodz.dormConnect.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormConnect.nfc.exception.DeviceNotFoundException;
import pl.lodz.dormConnect.nfc.mapper.NfcDeviceMapper;
import pl.lodz.dormConnect.users.exceptions.UserException;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class NfcDeviceService {

    private final NfcDeviceRepository nfcDeviceRepository;
    private final RoomAssignmentRepository roomAssignmentRepository;
    private final UserRepository userRepository;
    private final CommonRoomRepository commonRoomRepository;
    private final CommonRoomAssignmentRepository commonRoomAssignmentRepository;


    @Autowired
    public NfcDeviceService(NfcDeviceRepository nfcDeviceRepository,
                            RoomAssignmentRepository roomAssignmentRepository,
                            UserRepository userRepository,
                            CommonRoomAssignmentRepository commonRoomAssignmentRepository,
                            CommonRoomRepository commonRoomRepository) { 
        this.nfcDeviceRepository = nfcDeviceRepository;
        this.roomAssignmentRepository = roomAssignmentRepository;
        this.userRepository = userRepository;
        this.commonRoomAssignmentRepository = commonRoomAssignmentRepository;
        this.commonRoomRepository = commonRoomRepository;
    }

    public NfcDeviceRegisterDTO registerDevice(NfcDeviceRegisterDTO nfcDeviceDTO) {
        NfcDeviceEntity existingDevice = nfcDeviceRepository.findByUuid(UUID.fromString(nfcDeviceDTO.uuid()));
        
        // if the device alaready exisits, update it's information and continue
        if (existingDevice != null) {
            existingDevice.setDeviceStatus(nfcDeviceDTO.lockStatus());
            existingDevice.setRoomNumber(nfcDeviceDTO.roomNumber());
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

        UserEntity currentUser = userRepository.findByUuid(nfcAccessRequestDTO.user_uuid()).orElse(null);
        if (currentUser == null) {
            throw new RuntimeException("Validation failed");
        }

        // check if the user card uuid and the one from the request match
        if (currentUser.getCardUuid() != nfcAccessRequestDTO.card_uid()) {
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
        UserEntity currentUser = userRepository.findByUuid(nfcAccessRequestDTO.user_uuid()).orElse(null);
        if (currentUser == null) {
            throw new UserException("Error while fetching user with uuid: " + nfcAccessRequestDTO.user_uuid());
        }
        //long currentRoomId = commonRoomRepository.findCommonRoomByName()
        // TODO: Implement logic to fetch the current room ID based on the request
        // CommonRoomAssignment currentAssignment = commonRoomAssignmentRepository
        //         .findCurrentAssingmentByCommonRoomId(nfc)
        
        throw new UnsupportedOperationException("Unimplemented method 'checkCommonRoomAccess'");
    }

}

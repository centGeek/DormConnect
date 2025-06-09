package pl.lodz.dormConnect.nfc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pl.lodz.commons.entity.NfcDeviceEntity;
import pl.lodz.commons.entity.UserEntity;
import pl.lodz.commons.repository.jpa.NfcDeviceRepository;
import pl.lodz.commons.repository.jpa.RoomAssignmentRepository;
import pl.lodz.commons.repository.jpa.UserRepository;
import pl.lodz.dormConnect.nfc.dto.NfcAccessRequestDTO;
import pl.lodz.dormConnect.nfc.dto.NfcDeviceRegisterDTO;
import pl.lodz.dormConnect.nfc.dto.NfcDeviceUpdateDTO;
import pl.lodz.dormConnect.nfc.dto.NfcProgramCardDTO;
import pl.lodz.dormConnect.nfc.exception.DeviceNotFoundException;
import pl.lodz.dormConnect.nfc.mapper.NfcDeviceMapper;


import java.time.LocalDate;
import java.util.UUID;

@Service
public class NfcDeviceService {

    private final NfcDeviceRepository nfcDeviceRepository;
    private final RoomAssignmentRepository roomAssignmentRepository;
    private final UserRepository userRepository;


    @Autowired
    public NfcDeviceService(NfcDeviceRepository nfcDeviceRepository,
                            RoomAssignmentRepository roomAssignmentRepository,
                            UserRepository userRepository)  {
        this.nfcDeviceRepository = nfcDeviceRepository;
        this.roomAssignmentRepository = roomAssignmentRepository;
        this.userRepository = userRepository;
 
    }

    public NfcDeviceRegisterDTO registerDevice(NfcDeviceRegisterDTO nfcDeviceDTO) {
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
        return roomAssignmentRepository.existsAssignmentAtDate(
                currentUser.getId(),
                LocalDate.now(),
                Integer.toString(nfcAccessRequestDTO.roomNumber()));
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

}

package pl.lodz.dormitoryservice.nfc.mapper;



import pl.lodz.dormitoryservice.nfc.dto.GetNfcDeviceDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcDeviceRegisterDTO;
import pl.lodz.dormitoryservice.nfc.dto.NfcDeviceUpdateDTO;
import pl.lodz.dormitoryservice.nfc.entity.NfcDeviceEntity;

import java.util.UUID;

public class NfcDeviceMapper {

    public static NfcDeviceEntity registerDtoToEntity(NfcDeviceRegisterDTO nfcDevice) {
        NfcDeviceEntity nfcDeviceEntity = new NfcDeviceEntity();
        nfcDeviceEntity.setUuid(UUID.fromString(nfcDevice.uuid()));
        nfcDeviceEntity.setRoomNumber(nfcDevice.roomNumber());
        nfcDeviceEntity.setLockStatus(nfcDevice.lockStatus());
        return nfcDeviceEntity;
    }

    public static NfcDeviceRegisterDTO entityToRegisterDto(NfcDeviceEntity nfcDevice) {
        return new NfcDeviceRegisterDTO(
                nfcDevice.getUuid().toString(),
                nfcDevice.getRoomNumber(),
                nfcDevice.getLockStatus(),
                nfcDevice.getIpAddress(),
                nfcDevice.getMacAddress()
        );
    }

    public static NfcDeviceUpdateDTO entityToDeviceUpdateDTO(NfcDeviceEntity nfcDevice) {
        return new NfcDeviceUpdateDTO(
                nfcDevice.getUuid(),
                nfcDevice.getRoomNumber(),
                nfcDevice.getLockStatus()
        );
    }

    public static GetNfcDeviceDTO entityToGetDTO(NfcDeviceEntity nfcDevice) {
        return new GetNfcDeviceDTO(
                nfcDevice.getId(),
                nfcDevice.getRoomNumber(),
                nfcDevice.getDeviceStatus(),
                nfcDevice.getLockStatus(),
                nfcDevice.getIpAddress(),
                nfcDevice.getMacAddress()
        );
    }

}

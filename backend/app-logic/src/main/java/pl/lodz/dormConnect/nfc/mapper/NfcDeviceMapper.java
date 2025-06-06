package pl.lodz.dormConnect.nfc.mapper;

import pl.lodz.commons.entity.NfcDeviceEntity;

import pl.lodz.dormConnect.nfc.dto.NfcDeviceRegisterDTO;
import pl.lodz.dormConnect.nfc.dto.NfcDeviceUpdateDTO;


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
                nfcDevice.getLockStatus()
        );
    }

    public static NfcDeviceUpdateDTO entityToDeviceUpdateDTO(NfcDeviceEntity nfcDevice) {
        return new NfcDeviceUpdateDTO(
                nfcDevice.getUuid(),
                nfcDevice.getRoomNumber(),
                nfcDevice.getLockStatus()
        );
    }

}

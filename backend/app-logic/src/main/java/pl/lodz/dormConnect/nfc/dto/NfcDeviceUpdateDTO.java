package pl.lodz.dormConnect.nfc.dto;

import java.util.UUID;

public record NfcDeviceUpdateDTO(
        UUID device_uuid,
        int roomNumber,
        String lockStatus
) {
}

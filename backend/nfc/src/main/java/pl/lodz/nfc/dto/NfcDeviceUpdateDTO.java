package pl.lodz.nfc.dto;

import java.util.UUID;

public record NfcDeviceUpdateDTO(
        UUID device_uuid,
        String roomNumber,
        String lockStatus
) {
}

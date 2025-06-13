package pl.lodz.nfc.dto;

import java.util.UUID;

public record NfcAccessRequestDTO(
        String device_uuid,
        String card_uid,
        String user_uuid,
        String roomNumber,
        String lockStatus
) {
}

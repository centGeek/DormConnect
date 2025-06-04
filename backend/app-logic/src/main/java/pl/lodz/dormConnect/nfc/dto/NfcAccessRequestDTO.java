package pl.lodz.dormConnect.nfc.dto;

import java.util.UUID;

public record NfcAccessRequestDTO(
        String device_uuid,
        String card_uid,
        String user_uuid,
        int roomNumber,
        String lockStatus
) {
}

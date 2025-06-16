package pl.lodz.dormitoryservice.nfc.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record NfcAccessRequestDTO(
        String device_uuid,
        String card_uid,
        String user_uuid,
        String roomNumber,
        @Nullable
        String lockStatus
) {
}

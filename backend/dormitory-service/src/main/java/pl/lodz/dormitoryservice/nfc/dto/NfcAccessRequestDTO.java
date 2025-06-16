package pl.lodz.dormitoryservice.nfc.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record NfcAccessRequestDTO(
        String deviceUuid,
        String cardUid,
        String userUuid,
        String roomNumber,
        @Nullable
        String lockStatus
) {
}

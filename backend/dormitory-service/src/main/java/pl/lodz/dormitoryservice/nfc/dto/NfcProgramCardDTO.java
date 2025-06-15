package pl.lodz.dormitoryservice.nfc.dto;

import java.util.UUID;

public record NfcProgramCardDTO(
        String deviceUuid,
        String userUuid
) {
}

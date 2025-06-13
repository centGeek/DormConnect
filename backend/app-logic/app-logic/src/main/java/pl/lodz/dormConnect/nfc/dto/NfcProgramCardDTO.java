package pl.lodz.dormConnect.nfc.dto;

import java.util.UUID;

public record NfcProgramCardDTO(
        UUID deviceUuid,
        String userUuid
) {
}

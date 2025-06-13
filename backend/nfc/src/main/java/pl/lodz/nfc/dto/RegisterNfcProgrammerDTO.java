package pl.lodz.nfc.dto;

import java.util.UUID;

public record RegisterNfcProgrammerDTO(
        UUID uuid,
        int port,
        String ipAddress,
        String deviceStatus
) {
    
}

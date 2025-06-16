package pl.lodz.dormitoryservice.nfc.dto;

public record NfcProgrammerDTO(
     String uuid,
     int port,
     String ipAddress,
     String deviceStatus,
     String macAddress
) {
    
}

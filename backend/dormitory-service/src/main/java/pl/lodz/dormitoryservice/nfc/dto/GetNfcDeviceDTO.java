package pl.lodz.dormitoryservice.nfc.dto;

public record GetNfcDeviceDTO(
        Long id,
        String roomNumber,
        String deviceStatus,
        String lockStatus,
        String ipAddress,
        String macAddress

) {
    
}

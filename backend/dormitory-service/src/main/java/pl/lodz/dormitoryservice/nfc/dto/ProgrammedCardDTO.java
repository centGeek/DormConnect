package pl.lodz.dormitoryservice.nfc.dto;

public record ProgrammedCardDTO(
        String deviceId,
        String userUuid,
        String cardUuid
) {
    
}

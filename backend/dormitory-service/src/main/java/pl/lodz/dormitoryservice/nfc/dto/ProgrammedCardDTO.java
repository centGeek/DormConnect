package pl.lodz.dormitoryservice.nfc.dto;

public record ProgrammedCardDTO(
        String programmerDeviceUuid,
        String cardUuid,
        String userUuid
) {
    
}

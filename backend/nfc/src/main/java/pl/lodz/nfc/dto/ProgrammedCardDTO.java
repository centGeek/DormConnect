package pl.lodz.nfc.dto;

public record ProgrammedCardDTO(
        String programmerDeviceUuid,
        String cardUuid,
        String userUuid
) {
    
}

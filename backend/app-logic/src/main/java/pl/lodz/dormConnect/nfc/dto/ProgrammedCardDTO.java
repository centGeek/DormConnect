package pl.lodz.dormConnect.nfc.dto;

public record ProgrammedCardDTO(
        String programmerDeviceUuid,
        String cardUuid,
        String userUuid
) {
    
}

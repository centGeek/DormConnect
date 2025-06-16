package pl.lodz.dormitoryservice.nfc.dto;

public record NfcStatusDTO(
    String status,
    String ssid,
    String ip
) {
}

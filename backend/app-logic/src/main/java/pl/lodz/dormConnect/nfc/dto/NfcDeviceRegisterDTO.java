package pl.lodz.dormConnect.nfc.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NfcDeviceRegisterDTO(
        @NotNull
        String uuid,
        @NotNull
        Integer roomNumber,
        @NotNull
        String lockStatus
) {
}

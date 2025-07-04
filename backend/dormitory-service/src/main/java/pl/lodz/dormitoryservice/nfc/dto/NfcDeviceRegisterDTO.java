package pl.lodz.dormitoryservice.nfc.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NfcDeviceRegisterDTO(
        @NotNull
        String uuid,
        @NotNull
        String roomNumber,
        @NotNull
        String lockStatus,
        @Nullable
        String ipAddress,
        @Nullable
        String macAddress
) {
}

package pl.lodz.dormitoryservice.nfc.dto;

public record GetUserDTO(
        long id,
        String uuid,
        String userName,
        String email,
        String role,
        boolean isActive
) {
    
}
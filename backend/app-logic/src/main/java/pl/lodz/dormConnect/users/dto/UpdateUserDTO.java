package pl.lodz.dormConnect.users.dto;

public record UpdateUserDTO(
        String uuid,
        String userName,
        String email,
        String role,
        boolean isActive
) {
    
}


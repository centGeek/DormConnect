package pl.lodz.users.dto;

public record GetUserDTO(
        long id,
        String uuid,
        String userName,
        String email,
        String role,
        boolean isActive
) {
    
}

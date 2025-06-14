package pl.lodz.users.mappers;


import pl.lodz.entity.UserEntity;
import pl.lodz.users.dto.GetUserDTO;

public class UserMapper {
    public static GetUserDTO mapToGetUserDTO(UserEntity userEntity) {
        return new GetUserDTO(
                userEntity.getId(),
                userEntity.getUuid().toString(),
                userEntity.getUserName(),
                userEntity.getEmail(),
                userEntity.getRole().getRoleName(),
                userEntity.isActive()
        );
    }
}

package pl.lodz.dormConnect.users.services;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.lodz.commons.entity.RoleEntity;
import pl.lodz.commons.entity.UserEntity;
import pl.lodz.commons.repository.jpa.RoleJpaRepository;
import pl.lodz.commons.repository.jpa.UserRepository;
import pl.lodz.dormConnect.users.dto.GetUserDTO;
import pl.lodz.dormConnect.users.dto.UpdateUserDTO;
import pl.lodz.dormConnect.users.mappers.UserMapper;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleJpaRepository roleJpaRepository;

    public UserService(UserRepository userRepository, RoleJpaRepository roleJpaRepository) {
        this.userRepository = userRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    public List<GetUserDTO> getAllUsers() {
        try {
            return userRepository.findAll().stream()
                .map(UserMapper::mapToGetUserDTO)
                .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
    }

    public GetUserDTO updateUser(UpdateUserDTO entity) {
        if (entity == null || entity.uuid() == null) {
            throw new IllegalArgumentException("Invalid input: entity or uuid cannot be null");
        }
        UserEntity userEntity = userRepository.findByUuid(entity.uuid())
            .orElseThrow(() -> new IllegalArgumentException("User not found with uuid: " + entity.uuid()));
        RoleEntity roleEntity = roleJpaRepository.findByRole(userEntity.getRole().getRoleName());
        userEntity.setUuid(entity.uuid());
        userEntity.setUserName(entity.userName());
        userEntity.setEmail(entity.email());
        userEntity.setRole(roleEntity);
        userEntity.setActive(entity.isActive());
        userRepository.save(userEntity);
        return UserMapper.mapToGetUserDTO(userEntity);
    }
    
}

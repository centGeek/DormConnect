package pl.lodz.users.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.entity.RoleEntity;
import pl.lodz.entity.UserEntity;
import pl.lodz.repository.jpa.RoleJpaRepository;
import pl.lodz.repository.jpa.UserRepository;
import pl.lodz.users.dto.GetUserDTO;
import pl.lodz.users.dto.UpdateUserDTO;
import pl.lodz.users.exceptions.UserException;
import pl.lodz.users.mappers.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

    @Transactional
    public GetUserDTO updateUser(UpdateUserDTO entity) {
        if (entity == null || entity.uuid() == null) {
            throw new IllegalArgumentException("Invalid input: entity or uuid cannot be null");
        }
        UserEntity userEntity = userRepository.findByUuid(entity.uuid())
            .orElseThrow(() -> new IllegalArgumentException("User not found with uuid: " + entity.uuid()));
        RoleEntity roleEntity = roleJpaRepository.findByRole(entity.role());
        userEntity.setUuid(entity.uuid());
        userEntity.setUserName(entity.userName());
        userEntity.setEmail(entity.email());
        userEntity.setRole(roleEntity);
        userEntity.setActive(entity.isActive());
        UserEntity saved = userRepository.save(userEntity);
        return UserMapper.mapToGetUserDTO(saved);
    }

    public GetUserDTO getUserById(Long id) {
        // TODO Auto-generated method stub
        UserEntity currUser = userRepository.findById(id).get();
        if (currUser == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        return UserMapper.mapToGetUserDTO(currUser);
    }

    public String getUsernameById(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return user.getUserName();
    }

    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserException("User not found") );
        userRepository.delete(userEntity);
    }

    public GetUserDTO updateUserUuid(UpdateUserDTO entity) {
        if (entity == null || entity.uuid() == null) {
            throw new IllegalArgumentException("Invalid input: entity or uuid cannot be null");
        }
        UserEntity userEntity = userRepository.findByUuid(entity.uuid())
            .orElseThrow(() -> new UserException("User not found with uuid: " + entity.uuid()));

        UUID newUuid = UUID.randomUUID();
        userEntity.setUuid(newUuid.toString());
        UserEntity saved = userRepository.save(userEntity);
        return UserMapper.mapToGetUserDTO(saved);
    }

}

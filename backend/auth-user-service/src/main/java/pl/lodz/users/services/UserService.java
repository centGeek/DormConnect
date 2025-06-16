package pl.lodz.users.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.entity.ManagerEntity;
import pl.lodz.entity.RoleEntity;
import pl.lodz.entity.StudentEntity;
import pl.lodz.entity.UserEntity;
import pl.lodz.repository.ManagerRepository;
import pl.lodz.repository.jpa.RoleJpaRepository;
import pl.lodz.repository.jpa.StudentJpaRepository;
import pl.lodz.repository.jpa.ManagerJpaRepository;
import pl.lodz.repository.jpa.UserRepository;
import pl.lodz.users.dto.GetUserDTO;
import pl.lodz.users.dto.UpdateUserDTO;
import pl.lodz.users.exceptions.UserException;
import pl.lodz.users.mappers.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final StudentJpaRepository studentJpaRepository;
    private final ManagerJpaRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleJpaRepository roleJpaRepository, StudentJpaRepository studentJpaRepository, ManagerJpaRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.studentJpaRepository = studentJpaRepository;
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
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

    public String getFullNameUserById(Long id) {

        Optional<StudentEntity> optionalStudentEntity = studentJpaRepository.findByUserId(id);
        if (optionalStudentEntity.isEmpty()) {
            return "Brak danych";
        }
        StudentEntity studentEntity = optionalStudentEntity.get();

        return studentEntity.getName() + " " + studentEntity.getSurname();
    }

    public String getUsernameById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return user.getUserName();
    }

    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UserException("User not found"));
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

    public String changeUserPassword(Long id, String newPassword, String oldPassword) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));
        if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            throw new UserException("Old password does not match");
        } else {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            UserEntity saved = userRepository.save(userEntity);
            return "Password changed successfully for user: " + saved.getUserName();
        }
    }

    public Map<String, String> getUserNameAndSurnameById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id: " + id));
        if (user.getRole().getRoleName().equals("ADMIN")) {
            return Map.of("name", "-", "surname", "-");
        }
        if (user.getRole().getRoleName().equals("MANAGER")) {
            ManagerEntity manager = managerRepository.findByUserId(id)
                    .orElseThrow(() -> new UserException("Manager not found with user id: " + id));
            return Map.of("name", manager.getName(), "surname", manager.getSurname());
        }
        if (user.getRole().getRoleName().equals("STUDENT")) {
            StudentEntity student = studentJpaRepository.findByUserId(id)
                    .orElseThrow(() -> new UserException("Student not found with user id: " + id));
            return Map.of("name", student.getName(), "surname", student.getSurname());
        }
        return Map.of("name", "-", "surname", "-");
    }

}

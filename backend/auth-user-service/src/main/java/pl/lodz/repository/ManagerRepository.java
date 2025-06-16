package pl.lodz.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.lodz.UserAlreadyExistsException;
import pl.lodz.entity.ManagerEntity;
import pl.lodz.entity.RoleEntity;
import pl.lodz.entity.UserEntity;
import pl.lodz.mapper.ManagerMapper;
import pl.lodz.model.Manager;
import pl.lodz.repository.jpa.ManagerJpaRepository;
import pl.lodz.repository.jpa.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class ManagerRepository {
    private ManagerJpaRepository managerJpaRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void register(Manager manager){
        ManagerEntity managerEntity = ManagerMapper.mapToEntity(manager);
        encodePassword(managerEntity, manager);
        conditionsToNotCreateManager(manager);
        managerEntity.getUser().setRole(new RoleEntity(2L, "MANAGER"));
        managerEntity.getUser().setActive(true);
        managerEntity.getUser().setUuid(UUID.randomUUID().toString());
        managerJpaRepository.saveAndFlush(managerEntity);
    }

    public List<Manager> getAll(){
        return managerJpaRepository.findAll().stream()
                .map(ManagerMapper::mapFromEntity)
                .toList();
    }

    public Optional<Manager> getByEmail(String email){
        Optional<ManagerEntity> byEmail = managerJpaRepository.findByEmail(email);
        return byEmail.map(ManagerMapper::mapFromEntity);
    }

    private void conditionsToNotCreateManager(Manager manager) {
        if (isEmailAlreadyPresent(manager.user().getEmail())) {
            throw new UserAlreadyExistsException("User with email: [%s] already exists".formatted(manager.user().getEmail()));
        }
    }


    private void encodePassword(ManagerEntity managerEntity, Manager manager) {
        managerEntity.getUser().setPassword(passwordEncoder.encode(manager.user().getPassword()));
    }

    private boolean isEmailAlreadyPresent(String email) {
        Optional<UserEntity> existingManagerByEmail = userRepository.findByEmail(email);
        return existingManagerByEmail.isPresent();
    }

}


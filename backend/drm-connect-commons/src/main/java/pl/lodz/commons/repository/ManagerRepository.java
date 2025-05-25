package pl.lodz.commons.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.lodz.commons.UserAlreadyExistsException;
import pl.lodz.commons.entity.ManagerEntity;
import pl.lodz.commons.entity.RoleEntity;
import pl.lodz.commons.entity.UserEntity;
import pl.lodz.commons.model.Manager;
import pl.lodz.commons.repository.jpa.ManagerJpaRepository;
import pl.lodz.commons.repository.jpa.UserRepository;
import pl.lodz.commons.mapper.ManagerMapper;

import java.util.List;
import java.util.Optional;

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
        managerEntity.getUser().setRole(new RoleEntity(1L, "STUDENT"));
        managerEntity.getUser().setActive(true);
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

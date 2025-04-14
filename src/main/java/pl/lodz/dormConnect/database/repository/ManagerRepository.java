package pl.lodz.dormConnect.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.database.entity.ManagerEntity;
import pl.lodz.dormConnect.database.entity.UserEntity;
import pl.lodz.dormConnect.database.repository.jpa.ManagerJpaRepository;
import pl.lodz.dormConnect.database.repository.jpa.UserRepository;
import pl.lodz.dormConnect.database.repository.mapper.ManagerMapper;
import pl.lodz.dormConnect.domain.Manager;
import pl.lodz.dormConnect.domain.exception.UserAlreadyExistsException;

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
        if (IsEmailAlreadyPresent(manager.user().getEmail())) {
            throw new UserAlreadyExistsException("User with email: [%s] already exists".formatted(manager.user().getEmail()));
        }
    }


    private void encodePassword(ManagerEntity managerEntity, Manager manager) {
        managerEntity.getUser().setPassword(passwordEncoder.encode(manager.user().getPassword()));
    }

    private boolean IsEmailAlreadyPresent(String email) {
        Optional<UserEntity> existingManagerByEmail = userRepository.findByEmail(email);
        return existingManagerByEmail.isPresent();
    }

}

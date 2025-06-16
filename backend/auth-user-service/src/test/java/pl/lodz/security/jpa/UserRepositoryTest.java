package pl.lodz.security.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.entity.RoleEntity;
import pl.lodz.entity.UserEntity;
import pl.lodz.repository.jpa.RoleJpaRepository;
import pl.lodz.repository.jpa.UserRepository;
import pl.lodz.security.config.PostgresContainerConfig;
import pl.lodz.security.fixtures.SecurityFixture;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(PostgresContainerConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Test
    void thatUserIsCorrectlyRetrievedByEmail() {
        RoleEntity savedRole = roleJpaRepository.saveAndFlush(SecurityFixture.adminRole());

        UserEntity userEntity = SecurityFixture.adminEntity(savedRole);

        userRepository.saveAndFlush(userEntity);

        var actualUserEntity = userRepository.findByEmail(userEntity.getEmail()).orElseThrow();

        Assertions.assertEquals(userEntity.getEmail(), actualUserEntity.getEmail());
        Assertions.assertEquals(userEntity.getUserName(), actualUserEntity.getUserName());
        Assertions.assertEquals(userEntity.getRole().getRoleName(), actualUserEntity.getRole().getRoleName());
    }
}

package pl.lodz.commons.repository.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import pl.lodz.commons.config.TestApplication;
import pl.lodz.commons.entity.UserEntity;
import pl.lodz.commons.fixtures.SecurityFixture;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;
    @Test
    void thatUserIsCorrectlyRetrievedByEmail() {
        //when
        UserEntity userEntity = SecurityFixture.adminEntity();
        roleJpaRepository.saveAndFlush(userEntity.getRole());
        userRepository.saveAndFlush(userEntity);

        System.out.println(userRepository.findAll());
        var actualUserEntity = userRepository.findByEmail(userEntity.getEmail()).get();
        //then
        Assertions.assertEquals(userEntity, actualUserEntity);
    }

}
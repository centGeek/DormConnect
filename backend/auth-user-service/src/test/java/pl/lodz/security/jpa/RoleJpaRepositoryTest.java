package pl.lodz.security.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.entity.RoleEntity;
import pl.lodz.repository.jpa.RoleJpaRepository;
import pl.lodz.repository.jpa.UserRepository;
import pl.lodz.security.fixtures.StudentFixture;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RoleJpaRepositoryTest {

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void thatRoleIsRetrievedProperlyByRoleName() {
        // given
        RoleEntity studentRole = new RoleEntity("STUDENT");
        roleJpaRepository.saveAndFlush(studentRole);

        var studentEntity = StudentFixture.anyStudentEntity(studentRole);
        var userEntity = studentEntity.getUser();
        userRepository.saveAndFlush(userEntity);

        // when
        Optional<RoleEntity> foundRoleOpt = Optional.ofNullable(roleJpaRepository.findByRole("STUDENT"));

        // then
        Assertions.assertTrue(foundRoleOpt.isPresent(), "Role STUDENT should be present");
        RoleEntity foundRole = foundRoleOpt.get();

        Assertions.assertEquals(studentRole.getRoleName(), foundRole.getRoleName());
    }
}

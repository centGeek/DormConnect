package pl.lodz.security.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.entity.RoleEntity;
import pl.lodz.repository.jpa.RoleJpaRepository;
import pl.lodz.repository.jpa.StudentJpaRepository;
import pl.lodz.repository.jpa.UserRepository;
import pl.lodz.security.config.PostgresContainerConfig;
import pl.lodz.security.fixtures.StudentFixture;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(PostgresContainerConfig.class)
class StudentJpaRepositoryTest {

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Test
    void thatStudentIsSavedAndRetrievedCorrectly() {
        // given
        RoleEntity studentRole = new RoleEntity("STUDENT");
        roleJpaRepository.saveAndFlush(studentRole);

        var studentEntity = StudentFixture.anyStudentEntity(studentRole);
        var userEntity = studentEntity.getUser();
        userRepository.saveAndFlush(userEntity);

        // when
        var savedStudentEntity = studentJpaRepository.saveAndFlush(studentEntity);

        // then
        Assertions.assertEquals(studentEntity.getName(), savedStudentEntity.getName());
        Assertions.assertEquals(studentEntity.getSurname(), savedStudentEntity.getSurname());
        Assertions.assertEquals(studentEntity.getUser(), savedStudentEntity.getUser());
        Assertions.assertNotNull(savedStudentEntity.getId());

        var foundStudentOpt = studentJpaRepository.findById(savedStudentEntity.getId());
        Assertions.assertTrue(foundStudentOpt.isPresent());
        Assertions.assertEquals(savedStudentEntity, foundStudentOpt.get());
    }
}

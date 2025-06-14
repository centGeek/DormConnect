package pl.lodz.security.jpa;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import pl.lodz.repository.jpa.RoleJpaRepository;
import pl.lodz.repository.jpa.StudentJpaRepository;
import pl.lodz.repository.jpa.UserRepository;
import pl.lodz.security.config.TestApplication;
import pl.lodz.security.fixtures.StudentFixture;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("test")
class StudentJpaRepositoryTest {

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;
    @Test
    void thatStudentIsSavedAndRetrievedCorrectly(){
        //given
        var studentEntity = StudentFixture.anyStudentEntity();
        var userEntity = studentEntity.getUser();
        roleJpaRepository.saveAndFlush(userEntity.getRole());
        userRepository.saveAndFlush(userEntity);

        //when
        var actualStudentEntity = studentJpaRepository.saveAndFlush(studentEntity);

        //then
        Assertions.assertEquals(studentEntity, actualStudentEntity);
    }

}
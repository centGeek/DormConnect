package pl.lodz.dormConnect.database.repository.jpa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.fixtures.StudentFixture;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainersTestConfiguration.class)
class StudentJpaRepositoryTest {

    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Autowired
    private UserRepository userRepository;
    @Test
    void thatStudentIsSavedAndRetrievedCorrectly(){
        //given
        var studentEntity = StudentFixture.anyStudentEntity();
        var userEntity = studentEntity.getUser();
        userRepository.saveAndFlush(userEntity);

        //when
        var actualStudentEntity = studentJpaRepository.saveAndFlush(studentEntity);

        //then
        Assertions.assertEquals(studentEntity, actualStudentEntity);
    }

}
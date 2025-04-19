package pl.lodz.dormConnect.database.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.domain.Student;
import pl.lodz.dormConnect.fixtures.StudentFixture;

@SpringBootTest
@Import(PersistenceContainersTestConfiguration.class)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void thatStudentIsRegisteredAndRetrievedCorrectly() {
        Student student = StudentFixture.anyStudent();

        //when
        studentRepository.register(student);

        //then
        var actualStudent = studentRepository.getByEmail(student.user().getEmail()).get();
        Assertions.assertEquals(student, actualStudent);
    }

}
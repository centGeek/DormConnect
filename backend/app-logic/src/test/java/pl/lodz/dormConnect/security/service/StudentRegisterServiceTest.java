package pl.lodz.dormConnect.security.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.commons.model.Student;
import pl.lodz.dormConnect.fixtures.StudentFixture;

import java.util.List;

@SpringBootTest
@Import(PersistenceContainersTestConfiguration.class)
class StudentRegisterServiceTest {

    @Autowired
    private StudentRegisterService studentRegisterService;

    @Test
    void thatStudentIsRegisteredCorrectly(){
        //given
        Student student = StudentFixture.anyStudent();

        //when
        studentRegisterService.registerStudent(student);

        List<Student> allStudents = studentRegisterService.getAllStudents();

        //then
        Assertions.assertThat(allStudents)
                .extracting(Student::surname)
                .contains(student.surname());
    }
}
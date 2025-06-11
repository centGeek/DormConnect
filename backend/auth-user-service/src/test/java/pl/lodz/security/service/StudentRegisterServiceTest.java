package pl.lodz.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.lodz.model.Student;
import pl.lodz.repository.StudentRepository;
import pl.lodz.security.config.TestApplication;
import pl.lodz.security.fixtures.StudentFixture;
import pl.lodz.service.StudentRegisterService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@SpringBootTest
@Import(TestApplication.class)
@ActiveProfiles("test")
class StudentRegisterServiceTest {

    @MockitoBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentRegisterService studentRegisterService;

    @Test
    void shouldRegisterStudent() {
        Student student = StudentFixture.anyStudent();

        studentRegisterService.registerStudent(student);

        verify(studentRepository).register(any());
    }
}
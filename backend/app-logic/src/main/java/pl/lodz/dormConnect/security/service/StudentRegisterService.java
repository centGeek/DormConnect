package pl.lodz.dormConnect.security.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.commons.repository.StudentRepository;
import pl.lodz.commons.model.Student;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentRegisterService {

    private final StudentRepository studentRepository;
    public void registerStudent(Student student){
        studentRepository.register(student);
    }
    public List<Student> getAllStudents(){
        return studentRepository.getAll();
    }
}

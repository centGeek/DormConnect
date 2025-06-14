package pl.lodz.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.lodz.UserAlreadyExistsException;
import pl.lodz.entity.RoleEntity;
import pl.lodz.entity.StudentEntity;
import pl.lodz.entity.UserEntity;
import pl.lodz.mapper.StudentMapper;
import pl.lodz.model.Student;
import pl.lodz.repository.jpa.StudentJpaRepository;
import pl.lodz.repository.jpa.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class StudentRepository {

    private StudentJpaRepository studentJpaRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void register(Student student){
        StudentEntity studentEntity = StudentMapper.mapToEntity(student);
        encodePassword(studentEntity, student);
        conditionsToNotCreateStudent(student);
        studentEntity.getUser().setRole(new RoleEntity(3L, "STUDENT"));
        studentEntity.getUser().setActive(true);
        studentJpaRepository.saveAndFlush(studentEntity);
    }

    public List<Student> getAll(){
        return studentJpaRepository.findAll().stream()
                .map(StudentMapper::mapFromEntity)
                .toList();
    }

    public Optional<Student> getByEmail(String email){
        Optional<StudentEntity> byEmail = studentJpaRepository.findByEmail(email);
        return byEmail.map(StudentMapper::mapFromEntity);
    }

    private void conditionsToNotCreateStudent(Student student) {
        if (isEmailAlreadyPresent(student.user().getEmail())) {
            throw new UserAlreadyExistsException("User with email: [%s] already exists".formatted(student.user().getEmail()));
        }
    }


    private void encodePassword(StudentEntity studentEntity, Student student) {
        studentEntity.getUser().setPassword(passwordEncoder.encode(student.user().getPassword()));
    }

    private boolean isEmailAlreadyPresent(String email) {
        Optional<UserEntity> existingStudentByEmail = userRepository.findByEmail(email);
        return existingStudentByEmail.isPresent();
    }

}

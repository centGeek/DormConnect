package pl.lodz.dormConnect.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.database.entity.RoleEntity;
import pl.lodz.dormConnect.database.repository.jpa.RoleJpaRepository;
import pl.lodz.dormConnect.database.repository.jpa.StudentJpaRepository;
import pl.lodz.dormConnect.database.repository.jpa.UserRepository;
import pl.lodz.dormConnect.domain.Student;
import pl.lodz.dormConnect.database.entity.StudentEntity;
import pl.lodz.dormConnect.database.entity.UserEntity;
import pl.lodz.dormConnect.database.repository.mapper.StudentMapper;
import pl.lodz.dormConnect.domain.exception.UserAlreadyExistsException;

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
        studentEntity.getUser().setRole(new RoleEntity(1L, "STUDENT"));
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

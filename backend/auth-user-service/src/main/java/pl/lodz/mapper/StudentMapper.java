package pl.lodz.mapper;


import pl.lodz.entity.StudentEntity;
import pl.lodz.model.Student;

public class StudentMapper {
    public static StudentEntity mapToEntity(Student student) {
        return new StudentEntity(student.name(), student.surname(), student.user());
    }
    public static Student mapFromEntity(StudentEntity student) {
        return new Student(student.getId(), student.getName(), student.getSurname(), student.getUser());
    }
}


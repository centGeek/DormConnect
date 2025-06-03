package pl.lodz.commons.mapper;

import pl.lodz.commons.model.Student;
import pl.lodz.commons.entity.StudentEntity;

public class StudentMapper {
    public static StudentEntity mapToEntity(Student student) {
        return new StudentEntity(student.name(), student.surname(), student.user());
    }
    public static Student mapFromEntity(StudentEntity student) {
        return new Student(student.getId(), student.getName(), student.getSurname(), student.getUser());
    }
}

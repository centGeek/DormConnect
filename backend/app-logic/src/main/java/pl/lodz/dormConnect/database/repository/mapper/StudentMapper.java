package pl.lodz.dormConnect.database.repository.mapper;

import pl.lodz.dormConnect.domain.Student;
import pl.lodz.StudentEntity;

public class StudentMapper {
    public static StudentEntity mapToEntity(Student student) {
        return new StudentEntity(student.name(), student.surname(), student.user());
    }
    public static Student mapFromEntity(StudentEntity student) {
        return new Student(student.getId(), student.getName(), student.getSurname(), student.getUser());
    }
}

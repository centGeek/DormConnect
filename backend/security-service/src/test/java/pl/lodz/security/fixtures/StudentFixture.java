package pl.lodz.security.fixtures;

import pl.lodz.commons.model.Student;
import pl.lodz.commons.entity.StudentEntity;

public class StudentFixture {
    public static StudentEntity anyStudentEntity(){
        return new StudentEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Student anyStudent(){
        return new Student(1L, "Bolesław", "Chrobry", SecurityFixture.anotherUser());
    }
}

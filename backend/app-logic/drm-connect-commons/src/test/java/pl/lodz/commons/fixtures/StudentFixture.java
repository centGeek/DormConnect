package pl.lodz.commons.fixtures;

import pl.lodz.commons.entity.StudentEntity;
import pl.lodz.commons.model.Student;

public class StudentFixture {
    public static StudentEntity anyStudentEntity(){
        return new StudentEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Student anyStudent(){
        return new Student(1L, "Bolesław", "Chrobry", SecurityFixture.anotherUser());
    }
}

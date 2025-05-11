package pl.lodz.dormConnect.fixtures;

import pl.lodz.dormConnect.domain.Student;
import pl.lodz.StudentEntity;

public class StudentFixture {
    public static StudentEntity anyStudentEntity(){
        return new StudentEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Student anyStudent(){
        return new Student(1L, "Bolesław", "Chrobry", SecurityFixture.anotherUser());
    }
}

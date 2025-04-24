package pl.lodz.dormConnect.fixtures;

import pl.lodz.dormConnect.database.entity.StudentEntity;
import pl.lodz.dormConnect.domain.Student;

public class StudentFixture {
    public static StudentEntity anyStudentEntity(){
        return new StudentEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Student anyStudent(){
        return new Student(1L, "Bolesław", "Chrobry", SecurityFixture.anotherUser());
    }
}

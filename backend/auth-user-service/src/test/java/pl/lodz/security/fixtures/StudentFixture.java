package pl.lodz.security.fixtures;

import pl.lodz.entity.StudentEntity;
import pl.lodz.model.Student;
import pl.lodz.entity.RoleEntity;

public class StudentFixture {

    public static StudentEntity anyStudentEntity(RoleEntity role) {
        return new StudentEntity(
                "Bolesław",
                "Chrobry",
                SecurityFixture.anyUser(role)
        );
    }

    public static Student anyStudent() {
        return new Student(
                1L,
                "Bolesław",
                "Chrobry",
                SecurityFixture.anotherUser(SecurityFixture.studentRole())
        );
    }
}

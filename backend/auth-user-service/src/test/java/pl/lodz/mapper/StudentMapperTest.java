package pl.lodz.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.entity.RoleEntity;
import pl.lodz.entity.StudentEntity;
import pl.lodz.model.Student;
import pl.lodz.security.fixtures.StudentFixture;

class StudentMapperTest {

    @Test
    public void thatMappingFromEntityWorksCorrectly() {
        //given
        StudentEntity studentEntity = StudentFixture.anyStudentEntity(new RoleEntity());

        //when
        Student student = StudentMapper.mapFromEntity(studentEntity);

        //then
        Assertions.assertEquals(studentEntity.getName(), student.name());
        Assertions.assertEquals(studentEntity.getSurname(), student.surname());
    }

    @Test
    public void thatMappingToEntityWorksCorrectly() {
        //given
        Student student = StudentFixture.anyStudent();

        //when
        StudentEntity studentEntity = StudentMapper.mapToEntity(student);

        //then
        Assertions.assertEquals(studentEntity.getName(), student.name());
        Assertions.assertEquals(studentEntity.getSurname(), student.surname());
    }

}
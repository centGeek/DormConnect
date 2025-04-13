package pl.lodz.dormConnect.fixtures;

import pl.lodz.dormConnect.security.model.StudentEntity;

public class StudentFixture {
    public static StudentEntity anyStudent(){
        return new StudentEntity("Bolesław", "Chrobry", SecurityFixture.anyStudent());
    }
}

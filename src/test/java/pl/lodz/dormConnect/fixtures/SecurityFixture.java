package pl.lodz.dormConnect.fixtures;

import pl.lodz.dormConnect.security.model.RoleEntity;
import pl.lodz.dormConnect.security.model.UserEntity;

public class SecurityFixture {
    public static RoleEntity adminRole(){
        return new RoleEntity(1L, "ADMIN");
    }
    public static RoleEntity managerRole(){
        return new RoleEntity(2L, "MANAGER");
    }

    public static RoleEntity studentRole(){
        return new RoleEntity(3L, "STUDENT");
    }

    public static UserEntity adminEntity(){
        return new UserEntity(1L,
                "admin", "admin@edu.p.lodz.pl", "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true, adminRole());
    }

    public static UserEntity anyStudent(){
        return new UserEntity(
                "student", "student@edu.p.lodz.pl", "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true, studentRole());
    }
}

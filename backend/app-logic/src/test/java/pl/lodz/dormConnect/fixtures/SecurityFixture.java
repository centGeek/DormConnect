package pl.lodz.dormConnect.fixtures;

import pl.lodz.commons.entity.RoleEntity;
import pl.lodz.commons.entity.UserEntity;

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

    public static UserEntity anyUser(){
        return new UserEntity(
                "student", "student@edu.p.lodz.pl", "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true, studentRole());
    }

    public static UserEntity anotherUser(){
        return new UserEntity(
                "maciej_zielak", "greenman@edu.p.lodz.pl", "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true, studentRole());
    }
}

package pl.lodz.security.fixtures;

import pl.lodz.entity.RoleEntity;
import pl.lodz.entity.UserEntity;

public class SecurityFixture {

    public static RoleEntity adminRole() {
        return new RoleEntity("ADMIN");
    }

    public static RoleEntity managerRole() {
        return new RoleEntity("MANAGER");
    }

    public static RoleEntity studentRole() {
        return new RoleEntity("STUDENT");
    }

    public static UserEntity adminEntity(RoleEntity role) {
        return new UserEntity(
                "admin",
                "admin@edu.p.lodz.pl",
                "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true,
                role);
    }

    public static UserEntity anyUser(RoleEntity role) {
        return new UserEntity(
                "student",
                "student@edu.p.lodz.pl",
                "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true,
                role);
    }

    public static UserEntity anotherUser(RoleEntity role) {
        return new UserEntity(
                "maciej_zielak",
                "greenman@edu.p.lodz.pl",
                "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true,
                role);
    }

    public static UserEntity managerUser(RoleEntity role) {
        return new UserEntity(
                "maciej_zielak",
                "managerxD@edu.p.lodz.pl",
                "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true,
                role);
    }
}

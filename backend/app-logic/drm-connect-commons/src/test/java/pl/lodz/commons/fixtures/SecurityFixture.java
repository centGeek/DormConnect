package pl.lodz.commons.fixtures;

public class SecurityFixture {
    public static RoleEntity adminRole(){
        return new RoleEntity("ADMIN");
    }
    public static RoleEntity managerRole(){
        return new RoleEntity("MANAGER");
    }

    public static RoleEntity studentRole(){
        return new RoleEntity("STUDENT");
    }

    public static UserEntity adminEntity(){
        return new UserEntity(
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

    public static UserEntity managerUser(){
        return new UserEntity(
                "maciej_zielak", "managerxD@edu.p.lodz.pl", "$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G",
                true, managerRole());
    }
}

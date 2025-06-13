package pl.lodz.commons.fixtures;

public class ManagerFixture {

    public static ManagerEntity anyManagerEntity(){
        return new ManagerEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Manager anyManager(){
        return new Manager(1L, "Bolesław", "Chrobry", SecurityFixture.managerUser());
    }
}

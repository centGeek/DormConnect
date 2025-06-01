package pl.lodz.commons.fixtures;

import pl.lodz.commons.entity.ManagerEntity;
import pl.lodz.commons.model.Manager;

public class ManagerFixture {

    public static ManagerEntity anyManagerEntity(){
        return new ManagerEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Manager anyManager(){
        return new Manager(1L, "Bolesław", "Chrobry", SecurityFixture.managerUser());
    }
}

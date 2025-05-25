package pl.lodz.dormConnect.fixtures;

import pl.lodz.dormConnect.domain.Manager;
import pl.lodz.entity.ManagerEntity;

public class ManagerFixture {

    public static ManagerEntity anyManagerEntity(){
        return new ManagerEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Manager anyManager(){
        return new Manager(1L, "Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
}

package pl.lodz.dormConnect.fixtures;

import pl.lodz.dormConnect.database.entity.ManagerEntity;
import pl.lodz.dormConnect.domain.Manager;

public class ManagerFixture {

    public static ManagerEntity anyManagerEntity(){
        return new ManagerEntity("Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
    public static Manager anyManager(){
        return new Manager(1L, "Bolesław", "Chrobry", SecurityFixture.anyUser());
    }
}

package pl.lodz.security.fixtures;

import pl.lodz.entity.ManagerEntity;
import pl.lodz.model.Manager;

public class ManagerFixture {

    public static ManagerEntity anyManagerEntity() {
        return new ManagerEntity(
                "Bolesław",
                "Chrobry",
                SecurityFixture.managerUser(SecurityFixture.managerRole())
        );
    }

    public static Manager anyManager() {
        return new Manager(
                1L,
                "Bolesław",
                "Chrobry",
                SecurityFixture.managerUser(SecurityFixture.managerRole())
        );
    }
}

package pl.lodz.commons.mapper;

import pl.lodz.commons.model.Manager;
import pl.lodz.commons.entity.ManagerEntity;

public class ManagerMapper {
    public static ManagerEntity mapToEntity(Manager manager) {
        return new ManagerEntity(manager.name(), manager.surname(), manager.user());
    }
    public static Manager mapFromEntity(ManagerEntity managerEntity) {
        return new Manager(managerEntity.getId(), managerEntity.getName(), managerEntity.getSurname(), managerEntity.getUser());
    }
}

package pl.lodz.dormConnect.database.repository.mapper;

import pl.lodz.dormConnect.domain.Manager;
import pl.lodz.entity.ManagerEntity;

public class ManagerMapper {
    public static ManagerEntity mapToEntity(Manager manager) {
        return new ManagerEntity(manager.name(), manager.surname(), manager.user());
    }
    public static Manager mapFromEntity(ManagerEntity managerEntity) {
        return new Manager(managerEntity.getId(), managerEntity.getName(), managerEntity.getSurname(), managerEntity.getUser());
    }
}

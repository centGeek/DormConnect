package pl.lodz.dormConnect.dorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormConnect.dorm.entities.GroupedRoomsEntity;

public interface GroupedRoomsRepository extends JpaRepository<GroupedRoomsEntity, Long> {
}

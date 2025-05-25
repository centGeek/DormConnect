package pl.lodz.dormConnect.dorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.entity.GroupedRoomsEntity;

public interface GroupedRoomsRepository extends JpaRepository<GroupedRoomsEntity, Long> {
}

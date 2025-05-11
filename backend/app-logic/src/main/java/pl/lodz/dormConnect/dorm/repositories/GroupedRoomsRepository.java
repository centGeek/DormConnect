package pl.lodz.dormConnect.dorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.GroupedRoomsEntity;

public interface GroupedRoomsRepository extends JpaRepository<GroupedRoomsEntity, Long> {
}

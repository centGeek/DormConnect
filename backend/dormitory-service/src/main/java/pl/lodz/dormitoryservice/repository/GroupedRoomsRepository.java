package pl.lodz.dormitoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormitoryservice.entity.GroupedRoomsEntity;

public interface GroupedRoomsRepository extends JpaRepository<GroupedRoomsEntity, Long> {
}

package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.commons.entity.GroupedRoomsEntity;

public interface GroupedRoomsRepository extends JpaRepository<GroupedRoomsEntity, Long> {
}

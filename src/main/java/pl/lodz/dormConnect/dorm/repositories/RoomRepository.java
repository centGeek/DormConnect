package pl.lodz.dormConnect.dorm.repositories;

import jakarta.persistence.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;

@Repository
public interface RoomRepository extends CrudRepository<RoomEntity, Long> {


}

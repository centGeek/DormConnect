package pl.lodz.dormConnect.dorm.repositories;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {


//    void countRoomEntity();
}

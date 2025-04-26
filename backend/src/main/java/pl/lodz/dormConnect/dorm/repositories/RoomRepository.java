package pl.lodz.dormConnect.dorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {


//    void countRoomEntity();
    @Query("SELECT r.capacity FROM RoomEntity r WHERE r.id = :roomId")
    int findCapacityById(@Param("roomId") Long roomId);

}

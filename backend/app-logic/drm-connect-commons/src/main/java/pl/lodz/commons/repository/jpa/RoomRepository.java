package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.commons.entity.RoomEntity;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {


//    void countRoomEntity();
    @Query("SELECT r.capacity FROM RoomEntity r WHERE r.id = :roomId")
    int findCapacityById(@Param("roomId") Long roomId);

    List<RoomEntity> findByFloor(int floor);
}

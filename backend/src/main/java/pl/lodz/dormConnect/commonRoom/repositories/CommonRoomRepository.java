package pl.lodz.dormConnect.commonRoom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoom;

import java.util.List;

public interface CommonRoomRepository extends JpaRepository<CommonRoom, Long> {
    CommonRoom findCommonRoomById(Long id);

    CommonRoom findCommonRoomByType(String type);

    List<CommonRoom> findCommonRoomByFloor(int floor);



    void deleteCommonRoomById(Long id);

    List<CommonRoom> findByFloor(int floor);
}

package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lodz.commons.entity.CommonRoomEntity;

import java.util.List;

@Repository
public interface CommonRoomRepository extends JpaRepository<CommonRoomEntity, Long> {
    CommonRoomEntity findCommonRoomById(Long id);


    CommonRoomEntity findCommonRoomByCommonRoomType(CommonRoomEntity.CommonRoomType commonRoomType);

    List<CommonRoomEntity> findCommonRoomByFloor(int floor);

    @Query("SELECT c FROM CommonRoomEntity c")
    List<CommonRoomEntity> getAllCommonRooms();

    void deleteCommonRoomById(Long id);

    List<CommonRoomEntity> findByFloor(int floor);
}
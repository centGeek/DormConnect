package pl.lodz.dormConnect.schedule.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.lodz.dormConnect.database.entity.UserEntity;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.schedule.entity.CommonRoom;
import pl.lodz.dormConnect.schedule.entity.CommonRoomAssigment;

import java.util.List;
import java.util.Optional;

public interface CommonRoomAssigmentRepository extends JpaRepository<CommonRoomAssigment, Long> {
    Optional<CommonRoomAssigment> findById(Long id);

    @Query("SELECT c FROM CommonRoomAssigment c JOIN c.users u WHERE u.id = :id")
    List<CommonRoomAssigment> findAssignmentsByUserId(@Param("id") Long id);

    CommonRoomAssigment findCommonRoomAssigmentByCommonRoom_Id(Long id);

    void deleteById(Long id);

    @Query("SELECT c FROM CommonRoomAssigment c WHERE c.archived = false")
    List<CommonRoomAssigment> findAllNotArchivedAssigments();
}

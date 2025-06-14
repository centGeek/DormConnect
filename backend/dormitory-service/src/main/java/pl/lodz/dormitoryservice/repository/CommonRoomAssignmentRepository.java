package pl.lodz.dormitoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommonRoomAssignmentRepository extends JpaRepository<CommonRoomAssignmentEntity, Long> {
    Optional<CommonRoomAssignmentEntity> findById(Long id);


    @Query("SELECT c FROM CommonRoomAssignmentEntity c JOIN c.commonRoom cr WHERE cr.id = :id")
    List<CommonRoomAssignmentEntity> getAssignmentsByCommonRoomId(@Param("id") Long id);

    void deleteById(Long id);

    @Query("SELECT c FROM CommonRoomAssignmentEntity c WHERE c.archived = false")
    List<CommonRoomAssignmentEntity> findAllNotArchivedAssigments();

    @Transactional
    void removeCommonRoomAssigmentsByCommonRoom(CommonRoomEntity commonRoom);

    @Transactional
    void removeAllByArchived(boolean archived);

    List<CommonRoomAssignmentEntity> getByArchived(boolean archived);

    List<CommonRoomAssignmentEntity> getByCommonRoomAndArchived(CommonRoomEntity commonRoom, boolean archived);

    @Query("""
            select crs from CommonRoomAssignmentEntity crs
            where crs.commonRoom.id = ?2
            and ?1 BETWEEN crs.startDate and crs.endDate
            """)
    CommonRoomAssignmentEntity findByCurrentDateAndCommonRoomId(String timestamp, long commonRoomId);

    @Query("""
            select crs from CommonRoomAssignmentEntity crs
            where crs.commonRoom.id = ?1
            and CURRENT_TIMESTAMP BETWEEN crs.startDate and crs.endDate
            """)
    CommonRoomAssignmentEntity findCurrentAssingmentByCommonRoomId(long commonRoomId);
}

package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.commons.entity.CommonRoomAssignmentEntity;
import pl.lodz.commons.entity.CommonRoomEntity;

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
}

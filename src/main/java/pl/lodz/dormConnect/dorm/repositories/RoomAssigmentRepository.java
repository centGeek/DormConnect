package pl.lodz.dormConnect.dorm.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.dorm.entities.RoomAssignEntity;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;

import java.time.LocalDate;
@Repository
public interface RoomAssigmentRepository extends CrudRepository<RoomAssignEntity, Long> {
    @Query("""
    SELECT COUNT(a) FROM RoomAssignEntity a
    WHERE a.room.id = :roomId
      AND (
            (a.toDate IS NULL OR a.toDate >= :startDate)
         AND (a.fromDate <= :endDate OR :endDate IS NULL)
      )
""")
    long countActiveAssignmentsForRoom(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM RoomAssignEntity a
    WHERE a.residentId = :studentId
      AND (
            (a.toDate IS NULL OR a.toDate >= :startDate)
         AND (a.fromDate <= :endDate OR :endDate IS NULL)
      )
""")
    boolean existsAssignmentForStudentDuring(@Param("studentId")Long studentId,@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}

package pl.lodz.dormitoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import pl.lodz.dormitoryservice.entity.RoomAssignEntity;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface RoomAssignmentRepository extends JpaRepository<RoomAssignEntity, Long> {
    @Query("""
    SELECT a FROM RoomAssignEntity a
    WHERE a.room.id = :roomId
      AND (
            (a.toDate IS NULL OR a.toDate >= :startDate)
         AND (a.fromDate <= :endDate)
      )
""")
    List<RoomAssignEntity> findAssignmentsForRoomInPeriod(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM RoomAssignEntity a
                WHERE a.residentId = :studentId
                  AND (
                        (a.toDate IS NULL OR a.toDate >= :startDate)
                     AND (a.fromDate <= :endDate)
                  )
            """)
    boolean existsAssignmentForStudentDuring(
            @Param("studentId") Long studentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT a
            FROM RoomAssignEntity a
            WHERE a.residentId = :studentId
            """)
    List<RoomAssignEntity> findAllAssignmentsByStudentId(@Param("studentId") Long studentId);


    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM RoomAssignEntity a
        JOIN RoomEntity r ON a.room.id = r.id
        WHERE a.residentId = :studentId
                AND (a.toDate IS NULL OR a.toDate >= :currDate)
                AND r.number = :roomNumber
        """)
    boolean existsAssignmentAtDate(@Param("studentId") Long studentId,
                                   @Param("currDate") LocalDate currDate,
                                   @Param("roomNumber") String roomNumber);

    @Query("SELECT r FROM RoomAssignEntity r WHERE r.residentId = :studentId AND r.room.id = :roomId AND (r.toDate IS NULL OR r.toDate >= CURRENT_DATE)")
    Optional<RoomAssignEntity> findCurrentAssignment(@Param("studentId") Long studentId, @Param("roomId") Long roomId);

    List<RoomAssignEntity> findByRoomIdAndToDateAfter(Long roomId, LocalDate now);
}

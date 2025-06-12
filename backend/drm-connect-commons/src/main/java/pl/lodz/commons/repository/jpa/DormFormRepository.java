package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.lodz.commons.entity.DormFormEntity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DormFormRepository extends JpaRepository<DormFormEntity, Long> {

    Collection<DormFormEntity> findByUserId(Long userId);

    List<DormFormEntity> findByIsProcessedFalseOrderByPriorityScoreDesc();

    @Query("""
                SELECT f FROM DormFormEntity f 
                WHERE f.userId = :userId 
                  AND f.isProcessed = false 
                  AND f.startDate <= :newEndDate 
                  AND f.endDate >= :newStartDate
            """)
    List<DormFormEntity> findConflictingUnprocessedForms(
            @Param("userId") Long userId,
            @Param("newStartDate") LocalDate newStartDate,
            @Param("newEndDate") LocalDate newEndDate
    );

}

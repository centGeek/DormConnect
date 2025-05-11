package pl.lodz.dormConnect.dorm.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.DormFormEntity;

import java.util.Collection;
import java.util.List;

public interface DormFormRepository extends JpaRepository<DormFormEntity, Long> {

    Collection<DormFormEntity> findByUserId(Long userId);

    List<DormFormEntity> findByIsProcessedFalseOrderByPriorityScoreDesc();
}

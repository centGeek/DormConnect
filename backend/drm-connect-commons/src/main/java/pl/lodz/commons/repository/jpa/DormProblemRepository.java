package pl.lodz.commons.repository.jpa;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lodz.commons.entity.DormProblemEntity;
import pl.lodz.commons.model.ProblemStatus;

import java.util.List;

@Repository
public interface DormProblemRepository extends JpaRepository<DormProblemEntity, Long> {

    @Transactional
    default DormProblemEntity updateOrInsert(DormProblemEntity dormProblemEntity) {
        return save(dormProblemEntity);
    }

    @Transactional
    @Query("SELECT d FROM DormProblemEntity d WHERE d.problemStatus = ?1")
    List<DormProblemEntity> findByProblemStatus(@NotNull ProblemStatus problemStatus);

    @Query("SELECT d FROM DormProblemEntity d WHERE d.studentId = ?1")
    List<DormProblemEntity> findByUserId(@NotNull Long id);

}

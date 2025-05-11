package pl.lodz.dormConnect.dormProblem.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;

import java.util.Optional;

@Repository
public interface DormProblemRepository extends JpaRepository<DormProblem, Long> {

    @Transactional
    default DormProblem updateOrInsert(DormProblem dormProblem) {
        return save(dormProblem);
    }

    @Transactional
    @Query("SELECT d FROM DormProblem d WHERE d.problemStatus = ?1")
    Optional<Object> findByProblemStatus(@NotNull ProblemStatus problemStatus);
}

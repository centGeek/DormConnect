package pl.lodz.dormConnect.dormProblem.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.dormProblem.dto.DormProblemDTO;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;

@Repository
public interface DormProblemRepository extends JpaRepository<DormProblem, Long> {

    @Transactional
    default DormProblem updateOrInsert(DormProblem dormProblem) {
        return save(dormProblem);
    }
}

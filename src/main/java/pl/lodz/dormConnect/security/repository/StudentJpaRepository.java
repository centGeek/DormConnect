package pl.lodz.dormConnect.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.security.model.StudentEntity;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long> {
}

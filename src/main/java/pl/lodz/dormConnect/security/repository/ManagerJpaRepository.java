package pl.lodz.dormConnect.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.security.model.ManagerEntity;

@Repository
public interface ManagerJpaRepository extends JpaRepository<ManagerEntity, Long> {
}

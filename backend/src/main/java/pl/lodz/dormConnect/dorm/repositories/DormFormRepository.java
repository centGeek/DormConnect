package pl.lodz.dormConnect.dorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormConnect.dorm.entities.DormFormEntity;

public interface DormFormRepository extends JpaRepository <DormFormEntity, Long> {

}

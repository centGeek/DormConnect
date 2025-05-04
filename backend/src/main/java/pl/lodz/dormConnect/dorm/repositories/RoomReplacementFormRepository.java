package pl.lodz.dormConnect.dorm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormConnect.dorm.entities.RoomReplacementFormEntity;

import java.util.List;

public interface RoomReplacementFormRepository extends JpaRepository<RoomReplacementFormEntity, Long> {
    List<RoomReplacementFormEntity> findAllByStatus(RoomReplacementFormEntity.FormStatus formStatus);
}

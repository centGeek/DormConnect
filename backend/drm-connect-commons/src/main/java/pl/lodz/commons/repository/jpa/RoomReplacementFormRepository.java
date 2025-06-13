package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.commons.entity.RoomReplacementFormEntity;

import java.util.List;

public interface RoomReplacementFormRepository extends JpaRepository<RoomReplacementFormEntity, Long> {
    List<RoomReplacementFormEntity> findAllByStatus(RoomReplacementFormEntity.FormStatus formStatus);
}
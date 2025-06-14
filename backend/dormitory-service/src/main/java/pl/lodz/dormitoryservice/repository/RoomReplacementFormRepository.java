package pl.lodz.dormitoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.dormitoryservice.entity.RoomReplacementFormEntity;


import java.util.List;

public interface RoomReplacementFormRepository extends JpaRepository<RoomReplacementFormEntity, Long> {
    List<RoomReplacementFormEntity> findAllByStatus(RoomReplacementFormEntity.FormStatus formStatus);
}
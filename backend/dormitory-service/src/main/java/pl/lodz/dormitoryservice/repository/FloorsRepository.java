package pl.lodz.dormitoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormitoryservice.entity.FloorEntity;

@Repository
public interface FloorsRepository extends JpaRepository<FloorEntity, Long> {
    FloorEntity findByFloorNumber(int floorNumber);
}

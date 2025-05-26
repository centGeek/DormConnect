package pl.lodz.dormConnect.floors.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.dormConnect.floors.entity.FloorEntity;

import java.util.Optional;

@Repository
public interface FloorsRepository extends JpaRepository<FloorEntity, Long> {
    FloorEntity findByFloorNumber(int floorNumber);
}

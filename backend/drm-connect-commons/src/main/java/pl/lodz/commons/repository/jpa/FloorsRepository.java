package pl.lodz.commons.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.commons.entity.FloorEntity;

import java.util.Optional;

@Repository
public interface FloorsRepository extends JpaRepository<FloorEntity, Long> {
    FloorEntity findByFloorNumber(int floorNumber);
}

package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.entity.FloorEntity;
import pl.lodz.dormitoryservice.fixtures.FloorFixtures;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FloorsRepositoryTest {

    @Autowired
    private FloorsRepository floorsRepository;
    @Test
    public void findByFloorNumbers(){
        //given
        FloorEntity floorEntity = FloorFixtures.anyFloorEntity();

        //when
        floorsRepository.save(floorEntity);

        //then
        FloorEntity actualByFloorNumber = floorsRepository.findByFloorNumber(floorEntity.getFloorNumber());
        Assertions.assertEquals(floorEntity.getFloorNumber(), actualByFloorNumber.getFloorNumber());

    }
}
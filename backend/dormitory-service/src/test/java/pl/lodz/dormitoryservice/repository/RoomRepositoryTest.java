package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.entity.RoomEntity;
import pl.lodz.dormitoryservice.fixtures.RoomFixtures;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void thatRoomRepositoryWorksCorrectly() {
        //given
        RoomEntity entity = RoomFixtures.anyRoomEntity();

        //when, then
        RoomEntity save = roomRepository.save(entity);
        List<RoomEntity> byFloor = roomRepository.findByFloor(save.getFloor());

        int actualCapacity = roomRepository.findCapacityById(save.getId());
        int expectedSize = 1;
        Assertions.assertEquals(expectedSize, byFloor.size());
        Assertions.assertEquals(entity.getCapacity(), actualCapacity);



    }
}
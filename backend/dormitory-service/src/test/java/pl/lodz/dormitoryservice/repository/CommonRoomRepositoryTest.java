package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;
import pl.lodz.dormitoryservice.fixtures.CommonRoomFixtures;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class CommonRoomRepositoryTest {


    @Autowired
    private CommonRoomRepository commonRoomRepository;

    @Test
    public void thatFindByIdWorksCorrectly() {
        //given
        int floor = 21111;
        CommonRoomEntity commonRoomEntity = CommonRoomFixtures.commonRoomEntityWithFloor(floor);
        commonRoomRepository.save(commonRoomEntity);

        //when
        List<CommonRoomEntity> commonRoomEntityList = commonRoomRepository.findByFloor(floor);

        //then
        int actualSize = 1;
        Assertions.assertEquals(commonRoomEntityList.size(), actualSize);
    }

}
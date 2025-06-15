package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.config.PostgresContainerConfig;
import pl.lodz.dormitoryservice.entity.RoomReplacementFormEntity;
import pl.lodz.dormitoryservice.fixtures.RoomReplacementFormFixtures;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(PostgresContainerConfig.class)
class RoomReplacementFormRepositoryTest {

    @Autowired
    private RoomReplacementFormRepository roomReplacementFormRepository;

    @Test
    public void thatRoomReplacementFormRepositoryWorksCorrectly() {
        //given
        RoomReplacementFormEntity roomReplacementFormEntity = RoomReplacementFormFixtures.roomReplacementFormEntity();
        roomReplacementFormRepository.save(roomReplacementFormEntity);

        //when
        List<RoomReplacementFormEntity> allByStatus = roomReplacementFormRepository.findAllByStatus(roomReplacementFormEntity.getStatus());

        ///then
        int expectedSize = 1;
        Assertions.assertEquals(expectedSize, allByStatus.size());
    }
}
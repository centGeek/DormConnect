package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.entity.GroupedRoomsEntity;
import pl.lodz.dormitoryservice.fixtures.GroupedRoomsFixtures;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GroupedRoomsRepositoryTest {

    @Autowired
    private GroupedRoomsRepository groupedRoomsRepository;


    @Test
    public void thatGroupedRoomsRepositoryWorksCorrectly() {
        //given
        GroupedRoomsEntity groupedRoomsEntity = GroupedRoomsFixtures.anyGroupedRoomsEntity();

        //when
        groupedRoomsRepository.save(groupedRoomsEntity);
        List<GroupedRoomsEntity> all = groupedRoomsRepository.findAll();

        //then
        int actualSize = 1;
        GroupedRoomsEntity actualGroupedRoomsEntity = all.getFirst();
        Assertions.assertEquals(groupedRoomsEntity.getGroupName(), actualGroupedRoomsEntity.getGroupName());
        Assertions.assertEquals(actualSize, all.size());
    }

}
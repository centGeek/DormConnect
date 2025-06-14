package pl.lodz.dormitoryservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormitoryservice.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;
import pl.lodz.dormitoryservice.fixtures.CommonRoomAssignmentsFixture;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class CommonRoomAssignmentRepositoryTest {

    @Autowired
    private CommonRoomAssignmentRepository commonRoomAssignmentRepository;

    @Autowired
    private CommonRoomRepository commonRoomRepository;

    @Test
    public void thatFindByIdWorksCorrectly() {
        //given
        CommonRoomAssignmentEntity commonRoomAssignmentEntity = CommonRoomAssignmentsFixture.anotherCommonRoomAssignmentEntity();
        commonRoomRepository.save(commonRoomAssignmentEntity.getCommonRoom());
        commonRoomAssignmentRepository.save(commonRoomAssignmentEntity);

        //when
        Optional<CommonRoomAssignmentEntity> byId = commonRoomAssignmentRepository.findById(1L);
        if(byId.isEmpty()){
            byId = commonRoomAssignmentRepository.findById(2L);
        }

        //then
        Assertions.assertEquals(commonRoomAssignmentEntity.getStartDate(), byId.get().getStartDate());
        Assertions.assertEquals(commonRoomAssignmentEntity.getEndDate(), byId.get().getEndDate());
    }

    @Test
    public void thatDeleteByIdWorksCorrectly() {
        //given
        CommonRoomAssignmentEntity commonRoomAssignmentEntity = CommonRoomAssignmentsFixture.anyCommonRoomAssignmentEntity();
        commonRoomRepository.save(commonRoomAssignmentEntity.getCommonRoom());
        commonRoomAssignmentRepository.save(commonRoomAssignmentEntity);

        //when
        int size = commonRoomAssignmentRepository.findAll().size();
        commonRoomAssignmentRepository.deleteById(1L);

        //then
        int expectedSize = size -1;
        int actualSize = commonRoomAssignmentRepository.findAll().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

}
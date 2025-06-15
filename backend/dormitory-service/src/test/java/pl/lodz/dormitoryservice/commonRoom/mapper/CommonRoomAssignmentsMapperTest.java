package pl.lodz.dormitoryservice.commonRoom.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomAssignmentGetDTO;
import pl.lodz.dormitoryservice.entity.CommonRoomAssignmentEntity;
import pl.lodz.dormitoryservice.fixtures.CommonRoomAssignmentsFixture;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CommonRoomAssignmentsMapperTest {

    @Test
    public void thatEntityIsMappedCorrectlyToDTO() {
        //given
        CommonRoomAssignmentEntity commonRoomAssignmentEntity = CommonRoomAssignmentsFixture.anyCommonRoomAssignmentEntity();

        //when
        CommonRoomAssignmentGetDTO actualCommonRoomAssignmentGetDTO =
                CommonRoomAssignmentsMapper.toCommonRoomAssignmentGetDTO(commonRoomAssignmentEntity, 1L);

        //then
        Assertions.assertEquals(commonRoomAssignmentEntity.getStartDate(), actualCommonRoomAssignmentGetDTO.startDate());
        Assertions.assertEquals(commonRoomAssignmentEntity.getEndDate(), actualCommonRoomAssignmentGetDTO.endDate());
        Assertions.assertEquals(commonRoomAssignmentEntity.isArchived(), actualCommonRoomAssignmentGetDTO.isArchived());
    }

    @Test
    public void thatEntitiesIsMappedCorrectlyToDTOs() {
        //given
        CommonRoomAssignmentEntity commonRoomAssignmentEntity = CommonRoomAssignmentsFixture.anyCommonRoomAssignmentEntity();
        CommonRoomAssignmentEntity anotherCommonRoomAssignmentEntity = CommonRoomAssignmentsFixture.anyCommonRoomAssignmentEntity();

        //when

        List<CommonRoomAssignmentGetDTO> commonRoomAssignmentGetDTOs = CommonRoomAssignmentsMapper.toCommonRoomAssignmentGetDTOs(
                List.of(commonRoomAssignmentEntity, anotherCommonRoomAssignmentEntity), 1L);

        //then
        Assertions.assertEquals(commonRoomAssignmentEntity.getStartDate(), commonRoomAssignmentGetDTOs.getFirst().startDate());
        Assertions.assertEquals(commonRoomAssignmentEntity.getEndDate(), commonRoomAssignmentGetDTOs.getFirst().endDate());
        Assertions.assertEquals(commonRoomAssignmentEntity.isArchived(), commonRoomAssignmentGetDTOs.getFirst().isArchived());

        Assertions.assertEquals(anotherCommonRoomAssignmentEntity.getStartDate(), commonRoomAssignmentGetDTOs.getLast().startDate());
        Assertions.assertEquals(anotherCommonRoomAssignmentEntity.getEndDate(), commonRoomAssignmentGetDTOs.getLast().endDate());
        Assertions.assertEquals(anotherCommonRoomAssignmentEntity.isArchived(), commonRoomAssignmentGetDTOs.getLast().isArchived());
    }
}
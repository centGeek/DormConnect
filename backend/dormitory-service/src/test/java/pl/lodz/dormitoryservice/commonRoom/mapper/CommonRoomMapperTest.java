package pl.lodz.dormitoryservice.commonRoom.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;
import pl.lodz.dormitoryservice.fixtures.CommonRoomFixtures;

@ExtendWith(MockitoExtension.class)
public class CommonRoomMapperTest {

    @Test
    public void thatMappingToCommonRoomGetDTOWorksCorrectly() {
        //given
        CommonRoomEntity commonRoomEntity = CommonRoomFixtures.anyCommonRoomEntity();

        //when
        CommonRoomGetDTO actualCommonRoomGetDTO = CommonRoomMapper.toCommonRoomGetDTO(commonRoomEntity);

        //then
        Assertions.assertEquals(commonRoomEntity.getCapacity(), actualCommonRoomGetDTO.capacity());
        Assertions.assertEquals(commonRoomEntity.getFloor(), actualCommonRoomGetDTO.floor());
        Assertions.assertEquals(commonRoomEntity.getCommonRoomType(), actualCommonRoomGetDTO.type());
        Assertions.assertEquals(commonRoomEntity.getHowManyTimesAWeekYouCanUseIt(),
                actualCommonRoomGetDTO.timesAWeekYouCanUseIt());
    }

    @Test
    public void thatMappingToCommonRoomEntityWorksCorrectly() {
        //given
        CommonRoomCreateDTO commonRoomCreateDTO = CommonRoomFixtures.anyCommonRoomCreateDTO();

        //when
        CommonRoomEntity actualCommonRoomEntity = CommonRoomMapper.toCommonRoomEntity(commonRoomCreateDTO);

        //then
        Assertions.assertEquals(commonRoomCreateDTO.type(), actualCommonRoomEntity.getCommonRoomType());
        Assertions.assertEquals(commonRoomCreateDTO.capacity(), actualCommonRoomEntity.getCapacity());
        Assertions.assertEquals(commonRoomCreateDTO.floor(), actualCommonRoomEntity.getFloor());
        Assertions.assertEquals(commonRoomCreateDTO.maxTimeYouCanStay(), actualCommonRoomEntity.getHoursOfTimeWindows());
        Assertions.assertEquals(commonRoomCreateDTO.howManyTimesAWeekYouCanUseIt(),
                actualCommonRoomEntity.getHowManyTimesAWeekYouCanUseIt());
    }

}
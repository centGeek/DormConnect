package pl.lodz.dormitoryservice.commonRoom.mapper;

import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormitoryservice.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormitoryservice.entity.CommonRoomEntity;

import java.util.List;


public class CommonRoomMapper {
    public static CommonRoomEntity toCommonRoomEntity(CommonRoomCreateDTO commonRoomCreateDTO) {
        CommonRoomEntity commonRoomEntity = new CommonRoomEntity();
        commonRoomEntity.setCommonRoomType(commonRoomCreateDTO.type());
        commonRoomEntity.setCapacity(commonRoomCreateDTO.capacity());
        commonRoomEntity.setFloor(commonRoomCreateDTO.floor());
        commonRoomEntity.setHoursOfTimeWindows(commonRoomCreateDTO.maxTimeYouCanStay());
        commonRoomEntity.setHowManyTimesAWeekYouCanUseIt(commonRoomCreateDTO.howManyTimesAWeekYouCanUseIt());

        return commonRoomEntity;

    }

    public static CommonRoomGetDTO toCommonRoomGetDTO(CommonRoomEntity commonRoomEntity) {
        return new CommonRoomGetDTO(
                commonRoomEntity.getId(),
                commonRoomEntity.getCommonRoomType(),
                commonRoomEntity.getFloor(),
                commonRoomEntity.getCapacity(),
                commonRoomEntity.getHowManyTimesAWeekYouCanUseIt()
        );
    }

    public static List<CommonRoomGetDTO> toCommonRoomGetDTOs(List<CommonRoomEntity> commonRoomEntities) {
        return commonRoomEntities.stream()
                .map(CommonRoomMapper::toCommonRoomGetDTO)
                .toList();
    }
}

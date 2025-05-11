package pl.lodz.dormConnect.commonRoom.mapper;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomCreateDTO;
import pl.lodz.dormConnect.commonRoom.dto.CommonRoomGetDTO;
import pl.lodz.dormConnect.commonRoom.entity.CommonRoomEntity;

import java.util.List;


@Component
public class CommonRoomMapper {
    public CommonRoomEntity toCommonRoomEntity(CommonRoomCreateDTO commonRoomCreateDTO) {
        CommonRoomEntity commonRoomEntity = new CommonRoomEntity();
        commonRoomEntity.setCommonRoomType(commonRoomCreateDTO.type());
        commonRoomEntity.setCapacity(commonRoomCreateDTO.capacity());
        commonRoomEntity.setFloor(commonRoomCreateDTO.floor());
        commonRoomEntity.setHoursOfTimeWindows(commonRoomCreateDTO.maxTimeYouCanStay());
        commonRoomEntity.setHowManyTimesAWeekYouCanUseIt(commonRoomCreateDTO.howManyTimesAWeekYouCanUseIt());

        return commonRoomEntity;

    }

    public CommonRoomGetDTO toCommonRoomGetDTO(CommonRoomEntity commonRoomEntity) {
        return new CommonRoomGetDTO(
                commonRoomEntity.getId(),
                commonRoomEntity.getCommonRoomType(),
                commonRoomEntity.getFloor(),
                commonRoomEntity.getCapacity()
        );
    }

    public List<CommonRoomGetDTO> toCommonRoomGetDTOs(List<CommonRoomEntity> commonRoomEntities) {
        return commonRoomEntities.stream()
                .map(this::toCommonRoomGetDTO)
                .toList();
    }
}

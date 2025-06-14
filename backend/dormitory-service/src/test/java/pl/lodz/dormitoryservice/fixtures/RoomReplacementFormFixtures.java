package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.entity.RoomReplacementFormEntity;

import java.time.LocalDateTime;

public class RoomReplacementFormFixtures {

    public static RoomReplacementFormEntity roomReplacementFormEntity() {
        return new RoomReplacementFormEntity(21321L,1L, 1L, 1L, RoomReplacementFormEntity.FormStatus.PENDING,
                LocalDateTime.now(), LocalDateTime.now().plusDays(2));
    }
}

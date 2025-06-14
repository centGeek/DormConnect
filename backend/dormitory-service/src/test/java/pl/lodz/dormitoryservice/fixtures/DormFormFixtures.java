package pl.lodz.dormitoryservice.fixtures;

import pl.lodz.dormitoryservice.entity.DormFormEntity;

import java.time.LocalDate;

public class DormFormFixtures {
    public static DormFormEntity anyDormFormEntity(){
        return new DormFormEntity(1L, LocalDate.now(), LocalDate.now().plusDays(1),
                false, "No comments", 1);
    }

    public static DormFormEntity conflictingDormFormEntity(){
        return new DormFormEntity(1L, LocalDate.now().plusDays(1), LocalDate.now(),
                false, "No comments", 1);
    }
}

package pl.lodz.dormproblemservice.fixtures;

import pl.lodz.dormproblemservice.ProblemStatus;
import pl.lodz.dormproblemservice.dto.GetDormProblemDTO;
import pl.lodz.dormproblemservice.entity.DormProblemEntity;

import java.time.LocalDate;

public class DormProblemFixtures {


    public static DormProblemEntity anyDormProblemEntity() {
        return new DormProblemEntity(3L, "3th Dorm", "Your description", "your answer",
                LocalDate.now(), LocalDate.now().plusDays(3), ProblemStatus.RESOLVED);
    }

    public static GetDormProblemDTO anyDormProblemDTO() {
        return new GetDormProblemDTO(3L, 3L, "username", "3th Dorm", "Your description", "your answer",
                LocalDate.now().plusDays(3), LocalDate.now(), ProblemStatus.RESOLVED);
    }
}

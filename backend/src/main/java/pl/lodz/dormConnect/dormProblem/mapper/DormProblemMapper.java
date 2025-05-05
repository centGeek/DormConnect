package pl.lodz.dormConnect.dormProblem.mapper;

import pl.lodz.dormConnect.dormProblem.dto.DormProblemDTO;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;

public class DormProblemMapper {

    public static DormProblem mapToEntity(DormProblemDTO dormProblemDTO) {
        return new DormProblem(
                dormProblemDTO.id(),
                dormProblemDTO.studentId(),
                dormProblemDTO.description(),
                dormProblemDTO.problemDate(),
                dormProblemDTO.problemStatus());
    }

    public static DormProblemDTO mapToDTO(DormProblem dormProblem) {
        return new DormProblemDTO(
                dormProblem.getId(),
                dormProblem.getStudentId(),
                dormProblem.getDescription(),
                dormProblem.getProblemDate(),
                dormProblem.getProblemStatus()
        );
    }
}

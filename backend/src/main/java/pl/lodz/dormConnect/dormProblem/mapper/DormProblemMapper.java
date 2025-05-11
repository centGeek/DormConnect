package pl.lodz.dormConnect.dormProblem.mapper;

import pl.lodz.dormConnect.dormProblem.dto.CreateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.GetDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.UpdateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;

public class DormProblemMapper {

    public static DormProblem mapToGetEntity(GetDormProblemDTO getDormProblemDTO) {
        return new DormProblem(
                getDormProblemDTO.id(),
                getDormProblemDTO.studentId(),
                getDormProblemDTO.description(),
                getDormProblemDTO.answer(),
                getDormProblemDTO.problemDate(),
                getDormProblemDTO.submittedDate(),
                getDormProblemDTO.problemStatus());
    }

    public static GetDormProblemDTO mapToGetDTO(DormProblem dormProblem) {
        return new GetDormProblemDTO(
                dormProblem.getId(),
                dormProblem.getStudentId(),
                dormProblem.getDescription(),
                dormProblem.getAnswer(),
                dormProblem.getProblemDate(),
                dormProblem.getSubmittedDate(),
                dormProblem.getProblemStatus()
        );
    }

    public static CreateDormProblemDTO mapToCreateDTO(DormProblem dormProblem) {
        return new CreateDormProblemDTO(
                dormProblem.getStudentId(),
                dormProblem.getDescription(),
                dormProblem.getProblemDate()
        );
    }

    public static DormProblem mapCreateDTOToEntity(CreateDormProblemDTO createDormProblemDTO) {
        return new DormProblem(
                0,
                createDormProblemDTO.studentId(),
                createDormProblemDTO.description(),
                null,
                createDormProblemDTO.problemDate(),
                null,
                null
        );
    }

    public static DormProblem mapUpdateDTOToEntity(UpdateDormProblemDTO updateDormProblemDTO) {
        return new DormProblem(
                updateDormProblemDTO.id(),
                updateDormProblemDTO.studentId(),
                updateDormProblemDTO.description(),
                updateDormProblemDTO.answer(),
                null,
                null,
                updateDormProblemDTO.problemStatus()
        );
    }

    public static UpdateDormProblemDTO mapToUpdateDTO(DormProblem dormProblem) {
        return new UpdateDormProblemDTO(
                dormProblem.getId(),
                dormProblem.getStudentId(),
                dormProblem.getDescription(),
                dormProblem.getAnswer(),
                dormProblem.getProblemStatus()
        );
    }
}

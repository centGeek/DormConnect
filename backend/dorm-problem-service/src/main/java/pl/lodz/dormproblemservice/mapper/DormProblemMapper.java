package pl.lodz.dormproblemservice.mapper;


import pl.lodz.dormproblemservice.dto.CreateDormProblemDTO;
import pl.lodz.dormproblemservice.dto.GetDormProblemDTO;
import pl.lodz.dormproblemservice.dto.UpdateDormProblemDTO;
import pl.lodz.dormproblemservice.entity.DormProblemEntity;

public class DormProblemMapper {

    public static DormProblemEntity mapToGetEntity(GetDormProblemDTO getDormProblemDTO) {
        return new DormProblemEntity(
                getDormProblemDTO.id(),
                getDormProblemDTO.studentId(),
                getDormProblemDTO.name(),
                getDormProblemDTO.description(),
                getDormProblemDTO.answer(),
                getDormProblemDTO.problemDate(),
                getDormProblemDTO.submittedDate(),
                getDormProblemDTO.problemStatus());
    }

    public static GetDormProblemDTO mapToGetDTO(DormProblemEntity dormProblemEntity) {
        return new GetDormProblemDTO(
                dormProblemEntity.getId(),
                dormProblemEntity.getStudentId(),
                dormProblemEntity.getName(),
                dormProblemEntity.getDescription(),
                dormProblemEntity.getAnswer(),
                dormProblemEntity.getProblemDate(),
                dormProblemEntity.getSubmittedDate(),
                dormProblemEntity.getProblemStatus()
        );
    }

    public static CreateDormProblemDTO mapToCreateDTO(DormProblemEntity dormProblemEntity) {
        return new CreateDormProblemDTO(
                dormProblemEntity.getStudentId(),
                dormProblemEntity.getName(),
                dormProblemEntity.getDescription(),
                dormProblemEntity.getProblemDate()
        );
    }

    public static DormProblemEntity mapCreateDTOToEntity(CreateDormProblemDTO createDormProblemDTO) {
        return new DormProblemEntity(
                0,
                createDormProblemDTO.studentId(),
                createDormProblemDTO.name(),
                createDormProblemDTO.description(),
                null,
                createDormProblemDTO.problemDate(),
                null,
                null
        );
    }

    public static DormProblemEntity mapUpdateDTOToEntity(UpdateDormProblemDTO updateDormProblemDTO) {
        return new DormProblemEntity(
                updateDormProblemDTO.id(),
                updateDormProblemDTO.studentId(),
                updateDormProblemDTO.name(),
                updateDormProblemDTO.description(),
                updateDormProblemDTO.answer(),
                null,
                null,
                updateDormProblemDTO.problemStatus()
        );
    }

    public static UpdateDormProblemDTO mapToUpdateDTO(DormProblemEntity dormProblemEntity) {
        return new UpdateDormProblemDTO(
                dormProblemEntity.getId(),
                dormProblemEntity.getStudentId(),
                dormProblemEntity.getName(),
                dormProblemEntity.getDescription(),
                dormProblemEntity.getAnswer(),
                dormProblemEntity.getProblemStatus()
        );
    }
}

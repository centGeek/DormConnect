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

    public static GetDormProblemDTO mapToGetDTO(DormProblemEntity dormProblemEntity, String username) {
        return new GetDormProblemDTO(
                dormProblemEntity.getId(),
                dormProblemEntity.getStudentId(),
                username,
                dormProblemEntity.getName(),
                dormProblemEntity.getDescription(),
                dormProblemEntity.getAnswer(),
                dormProblemEntity.getProblemDate(),
                dormProblemEntity.getSubmittedDate(),
                dormProblemEntity.getProblemStatus()
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

    public static UpdateDormProblemDTO mapToUpdateDTO(DormProblemEntity dormProblemEntity, String username) {
        return new UpdateDormProblemDTO(
                dormProblemEntity.getId(),
                dormProblemEntity.getStudentId(),
                username,
                dormProblemEntity.getName(),
                dormProblemEntity.getDescription(),
                dormProblemEntity.getAnswer(),
                dormProblemEntity.getProblemStatus()
        );
    }
}

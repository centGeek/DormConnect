package pl.lodz.dormConnect.dormProblem.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.dormConnect.dormProblem.dto.GetDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DormProblemMapperTest {

    long id = 22;
    long studentId = 12;
    String description = "description";
    String answer = "answer";
    LocalDate problemDate = LocalDate.of(2023, 10, 10);
    LocalDate submittedDate = LocalDate.now();
    ProblemStatus problemStatus = ProblemStatus.SUBMITTED;
    DormProblem problem = new DormProblem(id, studentId, description, answer, problemDate, submittedDate, problemStatus);

    @Test
    void mapToGetEntity() {
        var dormProblemDTO = new GetDormProblemDTO(id, studentId, description, answer, problemDate, submittedDate, problemStatus);
        var dormProblem = DormProblemMapper.mapToGetEntity(dormProblemDTO);

        assertEquals(dormProblem.getId(), dormProblemDTO.id());
        assertEquals(dormProblem.getStudentId(), dormProblemDTO.studentId());
        assertEquals(dormProblem.getDescription(), dormProblemDTO.description());
        assertEquals(dormProblem.getProblemDate(), dormProblemDTO.problemDate());
        assertEquals(dormProblem.getProblemStatus(), dormProblemDTO.problemStatus());
    }

    @Test
    void mapToGetDTO() {
        var dormProblemDTO = new GetDormProblemDTO(id, studentId, description, problemDate, problemStatus);
        var dormProblem = DormProblemMapper.mapToGetEntity(dormProblemDTO);
        var mappedDTO = DormProblemMapper.mapToGetDTO(dormProblem);

        assertEquals(mappedDTO.id(), dormProblem.getId());
        assertEquals(mappedDTO.studentId(), dormProblem.getStudentId());
        assertEquals(mappedDTO.description(), dormProblem.getDescription());
        assertEquals(mappedDTO.problemDate(), dormProblem.getProblemDate());
        assertEquals(mappedDTO.problemStatus(), dormProblem.getProblemStatus());
    }
}
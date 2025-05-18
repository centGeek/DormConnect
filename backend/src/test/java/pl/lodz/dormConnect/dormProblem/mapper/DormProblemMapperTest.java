package pl.lodz.dormConnect.dormProblem.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.lodz.dormConnect.dormProblem.dto.GetDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DormProblemMapperTest {

    private final long id = 22;
    private final long studentId = 12;
    private final String description = "description";
    private final String answer = "answer";
    private final String name = "basicProblemName";
    private final LocalDate problemDate = LocalDate.of(2023, 10, 10);
    private final LocalDate submittedDate = LocalDate.now();
    private final ProblemStatus problemStatus = ProblemStatus.SUBMITTED;
    private final DormProblem problem = new DormProblem(id, studentId, name, description, answer, problemDate, submittedDate, problemStatus);

    @Test
    void mapToGetEntity() {
        var dormProblemDTO = new GetDormProblemDTO(id, studentId, name, description, answer, problemDate, submittedDate, problemStatus);
        var dormProblem = DormProblemMapper.mapToGetEntity(dormProblemDTO);

        assertEquals(dormProblemDTO.id(), dormProblem.getId());
        assertEquals(dormProblemDTO.studentId(), dormProblem.getStudentId());
        assertEquals(dormProblemDTO.name(), dormProblem.getName());
        assertEquals(dormProblemDTO.description(), dormProblem.getDescription());
        assertEquals(dormProblemDTO.answer(), dormProblem.getAnswer());
        assertEquals(dormProblemDTO.problemDate(), dormProblem.getProblemDate());
        assertEquals(dormProblemDTO.problemStatus(), dormProblem.getProblemStatus());
    }

    @Test
    void mapToGetDTO() {
        var dormProblemDTO = new GetDormProblemDTO(id, studentId, name, description, answer, problemDate, submittedDate, problemStatus);
        var dormProblem = DormProblemMapper.mapToGetEntity(dormProblemDTO);
        var mappedDTO = DormProblemMapper.mapToGetDTO(dormProblem);

        assertEquals(dormProblemDTO.id(), mappedDTO.id());
        assertEquals(dormProblemDTO.studentId(), mappedDTO.studentId());
        assertEquals(dormProblemDTO.name(), mappedDTO.name());
        assertEquals(dormProblemDTO.description(), mappedDTO.description());
        assertEquals(dormProblemDTO.answer(), mappedDTO.answer());
        assertEquals(dormProblemDTO.problemDate(), mappedDTO.problemDate());
        assertEquals(dormProblemDTO.problemStatus(), mappedDTO.problemStatus());
    }
}
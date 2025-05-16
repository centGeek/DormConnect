package pl.lodz.dormConnect.dormProblem.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.dormProblem.dto.CreateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.GetDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.exception.DormProblemNotFoundException;
import pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(PersistenceContainersTestConfiguration.class)
class DormProblemServiceTest {

    @Autowired
    private DormProblemService dormProblemService;
    private final int dormProblemId = 13;
    private final int dormProblemId2 = 14;
    private final int studentId = 1;
    private final int studentId2 = 2;

    private final String description = "Test problem";
    private final String description2 = "Test problem 2";

    private final String answer = "Test answer";
    private final String answer2 = "Test answer 2";

    private final String name = "Test problem name";
    private final String name2 = "Test problem name 2";

    private final LocalDate probemDate1 = LocalDate.of(2025, 05, 1);
    private final LocalDate probemDate2 = LocalDate.of(2025, 05, 2);

    private final ProblemStatus status1 = ProblemStatus.SUBMITTED;
    private final ProblemStatus status2 = ProblemStatus.RESOLVED;

    private final DormProblem dormProblem1 = new DormProblem(dormProblemId, studentId, name, description, answer, probemDate1, LocalDate.now(), status1);
    private final DormProblem dormProblem2 = new DormProblem(dormProblemId2, studentId2, name2, description2, answer, probemDate2, LocalDate.now(), status2);

    @BeforeEach
    public void initialize() {
        dormProblemService.deleteAll();
    }

    @Test
    void createDormProblemTest() {
        GetDormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToCreateDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToGetEntity(dormProblemService.getDormProblemById(created.id()));
        assertEquals(problem.getStudentId(), dormProblem1.getStudentId());
        assertEquals(problem.getDescription(), dormProblem1.getDescription());
        assertEquals(problem.getProblemDate(), dormProblem1.getProblemDate());
        assertEquals(problem.getProblemStatus(), dormProblem1.getProblemStatus());
        dormProblemService.deleteDormProblem(problem.getId());
    }

    @Test
    void updateDormProblem() {
        GetDormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToCreateDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToGetEntity(dormProblemService.getDormProblemById(created.id()));
        problem.setStudentId(studentId2);
        problem.setDescription(description2);
        problem.setProblemDate(probemDate2);
        problem.setProblemStatus(status2);
        GetDormProblemDTO updated = dormProblemService.updateDormProblem(DormProblemMapper.mapToUpdateDTO(problem));
        DormProblem updatedProblem = DormProblemMapper.mapToGetEntity(updated);

        // studentId cannot be updated after the problem is created
        assertNotEquals(updatedProblem.getStudentId(), dormProblem2.getStudentId());
        assertEquals(updatedProblem.getDescription(), dormProblem2.getDescription());
        // problemId cannot be updated after the problem is created
        assertNotEquals(updatedProblem.getProblemDate(), dormProblem2.getProblemDate());
        assertEquals(updatedProblem.getProblemStatus(), dormProblem2.getProblemStatus());
        dormProblemService.deleteDormProblem(updatedProblem.getId());
    }

    @Test
    void deleteDormProblem() {
        GetDormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToCreateDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToGetEntity(dormProblemService.getDormProblemById(created.id()));
        assertDoesNotThrow(() -> dormProblemService.deleteDormProblem(problem.getId()));
        assertThrows(DormProblemNotFoundException.class, () -> dormProblemService.getDormProblemById(problem.getId()));
    }


    @Test
    void getDormProblemById() {
        GetDormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToCreateDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToGetEntity(dormProblemService.getDormProblemById(created.id()));
        assertEquals(problem.getStudentId(), dormProblem1.getStudentId());
        assertEquals(problem.getDescription(), dormProblem1.getDescription());
        assertEquals(problem.getProblemDate(), dormProblem1.getProblemDate());
        assertEquals(problem.getProblemStatus(), dormProblem1.getProblemStatus());
        dormProblemService.deleteDormProblem(problem.getId());
    }
}
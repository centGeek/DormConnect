package pl.lodz.dormConnect.dormProblem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.parameters.P;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.dormProblem.dto.DormProblemDTO;
import pl.lodz.dormConnect.dormProblem.exception.DormProblemNotFoundException;
import pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;
import pl.lodz.dormConnect.dormProblem.repository.DormProblemRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(PersistenceContainersTestConfiguration.class)
class DormProblemServiceTest {

    @Autowired
    DormProblemService dormProblemService;
    int dormProblemId = 13;
    int dormProblemId2 = 14;
    int studentId = 1;
    int studentId2 = 2;

    String description = "Test problem";
    String description2 = "Test problem 2";

    LocalDate probemDate1 = LocalDate.of(2025, 05, 1);
    LocalDate probemDate2 = LocalDate.of(2025, 05, 2);

    ProblemStatus status1 = ProblemStatus.SUBMITTED;
    ProblemStatus status2 = ProblemStatus.RESOLVED;

    DormProblem dormProblem1 = new DormProblem(dormProblemId, studentId, description, probemDate1, status1);
    DormProblem dormProblem2 = new DormProblem(dormProblemId2, studentId2, description2, probemDate2, status2);

    @BeforeEach
    void initialize() {

    }

    @Test
    void createDormProblemTest() {
        DormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToEntity(dormProblemService.getDormProblemById(created.id()));
        assertEquals(problem.getStudentId(), dormProblem1.getStudentId());
        assertEquals(problem.getDescription(), dormProblem1.getDescription());
        assertEquals(problem.getProblemDate(), dormProblem1.getProblemDate());
        assertEquals(problem.getProblemStatus(), dormProblem1.getProblemStatus());
        dormProblemService.deleteDormProblem(problem.getId());
    }

    @Test
    void updateDormProblem() {
        DormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToEntity(dormProblemService.getDormProblemById(created.id()));
        problem.setStudentId(studentId2);
        problem.setDescription(description2);
        problem.setProblemDate(probemDate2);
        problem.setProblemStatus(status2);
        DormProblemDTO updated = dormProblemService.updateDormProblem(DormProblemMapper.mapToDTO(problem));
        DormProblem updatedProblem = DormProblemMapper.mapToEntity(updated);

        assertEquals(updatedProblem.getStudentId(), dormProblem2.getStudentId());
        assertEquals(updatedProblem.getDescription(), dormProblem2.getDescription());
        assertEquals(updatedProblem.getProblemDate(), dormProblem2.getProblemDate());
        assertEquals(updatedProblem.getProblemStatus(), dormProblem2.getProblemStatus());
        dormProblemService.deleteDormProblem(updatedProblem.getId());
    }

    @Test
    void deleteDormProblem() {
        DormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToEntity(dormProblemService.getDormProblemById(created.id()));
        assertDoesNotThrow(() -> dormProblemService.deleteDormProblem(problem.getId()));
        assertThrows(DormProblemNotFoundException.class, () -> dormProblemService.getDormProblemById(problem.getId()));
    }

    @Test
    void getAllDormProblems() {
        DormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToDTO(dormProblem1));
        DormProblemDTO created2 = dormProblemService.createDormProblem(DormProblemMapper.mapToDTO(dormProblem2));
        assertEquals(2, dormProblemService.getAllDormProblems().size());
        dormProblemService.deleteDormProblem(created.id());
        dormProblemService.deleteDormProblem(created2.id());
    }

    @Test
    void getDormProblemById() {
        DormProblemDTO created = dormProblemService.createDormProblem(DormProblemMapper.mapToDTO(dormProblem1));
        DormProblem problem = DormProblemMapper.mapToEntity(dormProblemService.getDormProblemById(created.id()));
        assertEquals(problem.getStudentId(), dormProblem1.getStudentId());
        assertEquals(problem.getDescription(), dormProblem1.getDescription());
        assertEquals(problem.getProblemDate(), dormProblem1.getProblemDate());
        assertEquals(problem.getProblemStatus(), dormProblem1.getProblemStatus());
        dormProblemService.deleteDormProblem(problem.getId());
    }
}
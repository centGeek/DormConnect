package pl.lodz.dormproblemservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;
import pl.lodz.dormproblemservice.ProblemStatus;
import pl.lodz.dormproblemservice.dto.CreateDormProblemDTO;
import pl.lodz.dormproblemservice.dto.GetDormProblemDTO;
import pl.lodz.dormproblemservice.dto.UpdateDormProblemDTO;
import pl.lodz.dormproblemservice.entity.DormProblemEntity;
import pl.lodz.dormproblemservice.exception.DormProblemNotFoundException;
import pl.lodz.dormproblemservice.exception.IllegalProblemStatusChangeException;
import pl.lodz.dormproblemservice.fixtures.DormProblemFixtures;
import pl.lodz.dormproblemservice.repository.DormProblemRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DormProblemServiceTest {

    @Mock
    private DormProblemRepository dormProblemRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DormProblemService dormProblemService;

    private DormProblemEntity exampleEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exampleEntity = DormProblemFixtures.anyDormProblemEntity();
    }

    @Test
    void createDormProblemShouldReturnCreatedDTO() {
        CreateDormProblemDTO createDto = new CreateDormProblemDTO(
                1L,
                "3th Dorm",
                "Your description",
                LocalDate.now().plusDays(3)
        );

        when(jwtService.getIdFromToken("jwt-token")).thenReturn(3L);
        when(dormProblemRepository.save(any(DormProblemEntity.class))).thenReturn(exampleEntity);
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("username");

        GetDormProblemDTO result = dormProblemService.createDormProblem(createDto, "jwt-token");

        assertNotNull(result);
        assertEquals(exampleEntity.getId(), result.id());
        verify(dormProblemRepository, times(1)).save(any(DormProblemEntity.class));
    }

    @Test
    void updateDormProblemShouldUpdateAndReturnDTO() {
        UpdateDormProblemDTO updateDto = new UpdateDormProblemDTO(
                3L,
                3L,
                "Updated description",
                "Updated answer",
                "description",
                "answer",
                ProblemStatus.RESOLVED
        );

        when(dormProblemRepository.findById(3L)).thenReturn(Optional.of(exampleEntity));
        when(dormProblemRepository.save(any(DormProblemEntity.class))).thenReturn(exampleEntity);
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("username");

        GetDormProblemDTO result = dormProblemService.updateDormProblem(updateDto);

        assertNotNull(result);
        assertEquals(exampleEntity.getId(), result.id());
        verify(dormProblemRepository, times(1)).save(any(DormProblemEntity.class));
    }

    @Test
    void updateDormProblemShouldThrowIllegalProblemStatusChangeException() {

        UpdateDormProblemDTO updateDto = new UpdateDormProblemDTO(
                3L,
                3L,
                "Updated description",
                "Updated answer",
                "description",
                "answer",
                ProblemStatus.SUBMITTED
        );

        when(dormProblemRepository.findById(3L)).thenReturn(Optional.of(exampleEntity));

        IllegalProblemStatusChangeException ex = assertThrows(IllegalProblemStatusChangeException.class,
                () -> dormProblemService.updateDormProblem(updateDto));
        assertTrue(ex.getMessage().contains("Cannot change status"));
    }

    @Test
    void deleteDormProblemShouldDeleteEntity() {
        when(dormProblemRepository.findById(3L)).thenReturn(Optional.of(exampleEntity));
        doNothing().when(dormProblemRepository).delete(exampleEntity);

        dormProblemService.deleteDormProblem(3L);

        verify(dormProblemRepository, times(1)).delete(exampleEntity);
    }

    @Test
    void deleteDormProblemNotFoundShouldThrowException() {
        when(dormProblemRepository.findById(3L)).thenReturn(Optional.empty());

        DormProblemNotFoundException ex = assertThrows(DormProblemNotFoundException.class,
                () -> dormProblemService.deleteDormProblem(3L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void getAllDormProblemsAsAdminShouldReturnAll() {
        when(jwtService.getIdFromToken("jwt-token")).thenReturn(3L);
        when(jwtService.getRolesFromToken("jwt-token")).thenReturn(List.of("ADMIN"));
        when(dormProblemRepository.findAll()).thenReturn(List.of(exampleEntity));
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("username");

        List<GetDormProblemDTO> result = dormProblemService.getAllDormProblems("jwt-token");

        assertEquals(1, result.size());
        verify(dormProblemRepository, times(1)).findAll();
    }

    @Test
    void thatGetAllDormProblemsWorksCorrectly() {
        when(jwtService.getIdFromToken("jwt-token")).thenReturn(3L);
        when(jwtService.getRolesFromToken("jwt-token")).thenReturn(List.of("STUDENT"));
        when(dormProblemRepository.findByUserId(3L)).thenReturn(List.of(exampleEntity));
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("username");

        List<GetDormProblemDTO> result = dormProblemService.getAllDormProblems("jwt-token");

        assertEquals(1, result.size());
        verify(dormProblemRepository, times(1)).findByUserId(3L);
    }

    @Test
    void thatGetDormProblemByIdThrowsException() {
        when(dormProblemRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(DormProblemNotFoundException.class, () -> dormProblemService.getDormProblemById(3L));
    }

    @Test
    void thatGetAllProblemStatusesWorksCorrectly() {
        List<ProblemStatus> statuses = dormProblemService.getAllProblemStatuses();

        assertEquals(ProblemStatus.values().length, statuses.size());
    }
}

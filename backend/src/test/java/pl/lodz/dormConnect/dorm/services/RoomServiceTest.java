package pl.lodz.dormConnect.dorm.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
import pl.lodz.dormConnect.dorm.entities.RoomAssignEntity;
import pl.lodz.dormConnect.dorm.repositories.RoomAssignmentRepository;
import pl.lodz.dormConnect.dorm.repositories.RoomRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(PersistenceContainersTestConfiguration.class)
@WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
@SpringBootTest
public class RoomServiceTest {

    private RoomService roomService;
    private RoomRepository roomRepository;
    private RoomAssignmentRepository roomAssignmentRepository;

    @BeforeEach
    void setUp() {
        roomRepository = mock(RoomRepository.class);
        roomAssignmentRepository = mock(RoomAssignmentRepository.class);
        roomService = new RoomService(roomRepository, roomAssignmentRepository);
    }

    private RoomAssignEntity createAssignment(Long id, LocalDate from, LocalDate to) {
        RoomAssignEntity assign = new RoomAssignEntity();
        assign.setResidentId(id);
        assign.setFromDate(from);
        assign.setToDate(to);
        return assign;
    }

    @Test
    void testCanAssignNewResident_WhenRoomNotFull_ShouldReturnTrue() {
        Long roomId = 1L;
        LocalDate from = LocalDate.of(2025, 5, 1);
        LocalDate to = LocalDate.of(2025, 5, 5);

        List<RoomAssignEntity> existingAssignments = List.of(
                createAssignment(1L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 3)),
                createAssignment(2L, LocalDate.of(2025, 5, 2), LocalDate.of(2025, 5, 4))
        );

        when(roomRepository.findCapacityById(roomId)).thenReturn(3); // max 3 osoby
        when(roomAssignmentRepository.findAssignmentsForRoomInPeriod(roomId, from, to))
                .thenReturn(existingAssignments);

        boolean result = roomService.canAssignNewResident(roomId, from, to);

        assertTrue(result);
    }

    @Test
    void testCanAssignNewResident_WhenRoomWouldBeOverfull_ShouldReturnFalse() {
        Long roomId = 2L;
        LocalDate from = LocalDate.of(2025, 5, 1);
        LocalDate to = LocalDate.of(2025, 5, 5);

        List<RoomAssignEntity> existingAssignments = List.of(
                createAssignment(1L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5)),
                createAssignment(2L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5)),
                createAssignment(3L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 5))
        );

        when(roomRepository.findCapacityById(roomId)).thenReturn(3); // room is at max already
        when(roomAssignmentRepository.findAssignmentsForRoomInPeriod(roomId, from, to))
                .thenReturn(existingAssignments);

        boolean result = roomService.canAssignNewResident(roomId, from, to);

        assertFalse(result);
    }

    @Test
    void testCanAssignNewResident_WhenNewAssignmentIsOutsideExisting_ShouldReturnTrue() {
        Long roomId = 3L;
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        List<RoomAssignEntity> existingAssignments = List.of(
                createAssignment(1L, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 20))
        );

        when(roomRepository.findCapacityById(roomId)).thenReturn(2);
        when(roomAssignmentRepository.findAssignmentsForRoomInPeriod(roomId, from, to))
                .thenReturn(existingAssignments);

        boolean result = roomService.canAssignNewResident(roomId, from, to);

        assertTrue(result);
    }
}

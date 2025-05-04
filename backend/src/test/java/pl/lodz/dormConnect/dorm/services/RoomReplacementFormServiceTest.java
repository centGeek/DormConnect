//package pl.lodz.dormConnect.dorm.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.web.server.ResponseStatusException;
//import pl.lodz.dormConnect.config.PersistenceContainersTestConfiguration;
//import pl.lodz.dormConnect.dorm.entities.RoomAssignEntity;
//import pl.lodz.dormConnect.dorm.entities.RoomEntity;
//import pl.lodz.dormConnect.dorm.entities.RoomReplacementFormEntity;
//import pl.lodz.dormConnect.dorm.repositories.RoomAssignmentRepository;
//import pl.lodz.dormConnect.dorm.repositories.RoomReplacementFormRepository;
//import pl.lodz.dormConnect.dorm.repositories.RoomRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//@Import(PersistenceContainersTestConfiguration.class)
//@WithMockUser(username = "admin@edu.p.lodz.pl", authorities = "ADMIN")
//@SpringBootTest
//class RoomReplacementFormServiceTest {
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private RoomAssignmentRepository assignmentRepository;
//
//    @Autowired
//    private RoomReplacementFormRepository formRepository;
//
//    @Autowired
//    private RoomReplacementFormService roomReplacementFormService;
//
//    @Autowired
//    private RoomService roomService;
//
//    private Long studentA = 1L;
//    private Long studentB = 2L;
//    private RoomEntity room1;
//    private RoomEntity room2;
//
//    @BeforeEach
//    void setUp() {
//        assignmentRepository.deleteAll();
//        roomRepository.deleteAll();
//        formRepository.deleteAll();
//
//        room1 = new RoomEntity();
//        room1.setNumber("101");
//        room1.setCapacity(1);
//        room1.setFloor(1);
//        roomRepository.save(room1);
//
//        room2 = new RoomEntity();
//        room2.setNumber("102");
//        room2.setCapacity(1);
//        room2.setFloor(1);
//        roomRepository.save(room2);
//
//        LocalDate today = LocalDate.now();
//
//        // Student A -> room1
//        roomService.assignStudentToRoom(room1, studentA, today.minusDays(10), today.plusDays(10));
//
//        // Student B -> room2
//        roomService.assignStudentToRoom(room2, studentB, today.minusDays(10), today.plusDays(10));
//    }
//    @Rollback
//    @Test
//    void shouldSwapRoomsWithoutOverflow() {
//        // Given
//        RoomReplacementFormEntity form = new RoomReplacementFormEntity();
//        form.setRequesterId(studentA);
//        form.setTargetStudent(studentB);
//        form.setRequesterRoom(room1.getId());
//        form.setTargetRoom(room2.getId());
//        form.setStatus(RoomReplacementFormEntity.FormStatus.PENDING);
//        formRepository.save(form);
//
//        // When
//        roomReplacementFormService.acceptRoomSwapForm(form.getId());
//
//        // Then
//        RoomReplacementFormEntity updatedForm = formRepository.findById(form.getId()).orElseThrow();
//        assertEquals(RoomReplacementFormEntity.FormStatus.ACCEPTED, updatedForm.getStatus());
//        assertNotNull(updatedForm.getRespondedAt());
//
//        List<RoomAssignEntity> assignmentsA = assignmentRepository.findAllAssignmentsByStudentId(studentA);
//        List<RoomAssignEntity> assignmentsB = assignmentRepository.findAllAssignmentsByStudentId(studentB);
//
//        assertEquals(2, assignmentsA.size()); // stary i nowy
//        assertEquals(2, assignmentsB.size());
//
//        RoomAssignEntity latestA = assignmentsA.stream().max((a, b) -> a.getFromDate().compareTo(b.getFromDate())).get();
//        RoomAssignEntity latestB = assignmentsB.stream().max((a, b) -> a.getFromDate().compareTo(b.getFromDate())).get();
//
//        assertEquals(room2.getId(), latestA.getRoom().getId()); // A trafił do pokoju B
//        assertEquals(room1.getId(), latestB.getRoom().getId()); // B trafił do pokoju A
//    }
//
//    @Rollback
//    @Test
//    void shouldFailToSwapRoomsDueToOverflow() {
//        // Room2 będzie pełny (2 osoby przy 1 miejscach)
//        Long studentC = 3L;
//        roomService.assignStudentToRoom(room2, studentC, LocalDate.now().minusDays(20), LocalDate.now().plusDays(20));
//
//        RoomReplacementFormEntity form = new RoomReplacementFormEntity();
//        form.setRequesterId(studentA);
//        form.setTargetStudent(studentB);
//        form.setRequesterRoom(room1.getId());
//        form.setTargetRoom(room2.getId());
//        form.setStatus(RoomReplacementFormEntity.FormStatus.PENDING);
//        formRepository.save(form);
//
//        assertThrows(ResponseStatusException.class, () -> {
//            roomReplacementFormService.acceptRoomSwapForm(form.getId());
//        });
//
//        RoomReplacementFormEntity rejected = formRepository.findById(form.getId()).orElseThrow();
//        assertEquals(RoomReplacementFormEntity.FormStatus.REJECTED, rejected.getStatus());
//    }
//
//    @Test
//    void shouldNotAcceptAlreadyRejectedForm() {
//        RoomReplacementFormEntity form = new RoomReplacementFormEntity();
//        form.setRequesterId(studentA);
//        form.setTargetStudent(studentB);
//        form.setRequesterRoom(room1.getId());
//        form.setTargetRoom(room2.getId());
//        form.setStatus(RoomReplacementFormEntity.FormStatus.REJECTED);
//        formRepository.save(form);
//
//        assertThrows(ResponseStatusException.class, () -> {
//            roomReplacementFormService.acceptRoomSwapForm(form.getId());
//        });
//    }
//
//    @Test
//    void shouldFailWhenRoomDoesNotExist() {
//        RoomReplacementFormEntity form = new RoomReplacementFormEntity();
//        form.setRequesterId(studentA);
//        form.setTargetStudent(studentB);
//        form.setRequesterRoom(9999L); // nieistniejący
//        form.setTargetRoom(room2.getId());
//        form.setStatus(RoomReplacementFormEntity.FormStatus.PENDING);
//        formRepository.save(form);
//
//        assertThrows(ResponseStatusException.class, () -> {
//            roomReplacementFormService.acceptRoomSwapForm(form.getId());
//        });
//    }
//
////    @Test
////    void shouldCancelForm() {
////        RoomReplacementFormEntity form = new RoomReplacementFormEntity();
////        form.setRequesterId(studentA);
////        form.setTargetStudent(studentB);
////        form.setRequesterRoom(room1.getId());
////        form.setTargetRoom(room2.getId());
////        form.setStatus(RoomReplacementFormEntity.FormStatus.PENDING);
////        formRepository.save(form);
////
////        roomReplacementFormService.cancelFormByRequester(studentA, form.getId());
////
////        RoomReplacementFormEntity cancelled = formRepository.findById(form.getId()).orElseThrow();
////        assertEquals(RoomReplacementFormEntity.FormStatus.CANCELLED, cancelled.getStatus());
////    }
//
//}

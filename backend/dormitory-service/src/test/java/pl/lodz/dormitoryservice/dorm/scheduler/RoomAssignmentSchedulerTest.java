package pl.lodz.dormitoryservice.dorm.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import pl.lodz.dormitoryservice.entity.DormFormEntity;
import pl.lodz.dormitoryservice.entity.RoomAssignEntity;
import pl.lodz.dormitoryservice.entity.RoomEntity;
import pl.lodz.dormitoryservice.repository.DormFormRepository;
import pl.lodz.dormitoryservice.repository.RoomAssignmentRepository;
import pl.lodz.dormitoryservice.repository.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomAssignmentSchedulerTest {

    @Mock
    private DormFormRepository dormFormRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomAssignmentRepository roomAssignRepository;

    @InjectMocks
    private RoomAssignmentScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void assignRoomsAssignsAvailableRoom_andMarksFormProcessed() {
        DormFormEntity form = new DormFormEntity();
        form.setUserId(1L);
        form.setStartDate(LocalDate.of(2025, 7, 1));
        form.setEndDate(LocalDate.of(2025, 7, 31));
        form.setProcessed(false);
        form.setPriorityScore(10);

        RoomEntity room = new RoomEntity();
        room.setId(1L);
        room.setCapacity(2);
        room.setRoomAssigns(new ArrayList<>());

        when(dormFormRepository.findByIsProcessedFalseOrderByPriorityScoreDesc()).thenReturn(List.of(form));
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(roomAssignRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(dormFormRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        scheduler.assignRooms();

        assertThat(form.isProcessed()).isTrue();
        verify(roomAssignRepository, times(1)).save(any());
        verify(dormFormRepository, times(1)).save(form);
    }


    @Test
    void isRoomAvailableReturnsFalseWhenRoomIsFull() {
        RoomEntity room = new RoomEntity();
        room.setCapacity(1);

        RoomAssignEntity assign1 = new RoomAssignEntity();
        assign1.setFromDate(LocalDate.of(2025, 6, 1));
        assign1.setToDate(LocalDate.of(2025, 6, 30));

        room.setRoomAssigns(List.of(assign1));

        boolean available = invokeIsRoomAvailable(room,
                LocalDate.of(2025, 6, 10),
                LocalDate.of(2025, 6, 20));

        assertThat(available).isFalse();
    }

    private boolean invokeIsRoomAvailable(RoomEntity room, LocalDate start, LocalDate end) {
        try {
            var method = RoomAssignmentScheduler.class.getDeclaredMethod("isRoomAvailable", RoomEntity.class, LocalDate.class, LocalDate.class);
            method.setAccessible(true);
            return (boolean) method.invoke(scheduler, room, start, end);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

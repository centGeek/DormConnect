package pl.lodz.dormitoryservice.floors.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import pl.lodz.dormitoryservice.commonRoom.service.CommonRoomService;
import pl.lodz.dormitoryservice.dorm.services.RoomService;
import pl.lodz.dormitoryservice.entity.FloorEntity;
import pl.lodz.dormitoryservice.repository.FloorsRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FloorsServiceTest {

    @Mock
    private FloorsRepository floorsRepository;

    @Mock
    private RoomService roomService;

    @Mock
    private CommonRoomService commonRoomService;

    @InjectMocks
    private FloorsService floorsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        floorsService.setCommonRoomService(commonRoomService, roomService);
    }

    @Test
    void getFloorsShouldReturnSortedFloorNumbersDesc() {
        FloorEntity f1 = new FloorEntity();
        f1.setFloorNumber(2);
        FloorEntity f2 = new FloorEntity();
        f2.setFloorNumber(1);
        when(floorsRepository.findAll()).thenReturn(List.of(f1, f2));

        List<Integer> floors = floorsService.getFloors();

        assertThat(floors).containsExactly(1, 2);
    }

    @Test
    void addFloorShouldCreateFloorWithNextNumber() {
        when(floorsRepository.findAll()).thenReturn(List.of());

        FloorEntity savedFloor = new FloorEntity();
        savedFloor.setFloorNumber(0);

        when(floorsRepository.save(any(FloorEntity.class))).thenReturn(savedFloor);

        FloorEntity newFloor = floorsService.addFloor();

        assertThat(newFloor.getFloorNumber()).isEqualTo(0);
        verify(floorsRepository).save(any(FloorEntity.class));
    }

    @Test
    void deleteFloorShouldDeleteLastFloorAndRooms() {

        FloorEntity floor = new FloorEntity();
        floor.setFloorNumber(1);
        floor.setCommonRooms(new ArrayList<>(List.of(10L)));
        floor.setRooms(new ArrayList<>(List.of(20L)));

        when(floorsRepository.findByFloorNumber(1)).thenReturn(floor);
        when(floorsRepository.findAll()).thenReturn(List.of(floor));

        ResponseEntity<String> response = floorsService.deleteFloor(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(commonRoomService).deleteCommonRoom(10L);
        verify(roomService).deleteRoomById(20L);
        verify(floorsRepository).delete(floor);
    }

    @Test
    void deleteFloorShouldReturn404ForNonexistentFloor() {
        when(floorsRepository.findByFloorNumber(99)).thenReturn(null);

        ResponseEntity<String> response = floorsService.deleteFloor(99);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void deleteFloorShouldReturn400WhenNotLastFloor() {
        FloorEntity floor1 = new FloorEntity();
        floor1.setFloorNumber(0);
        FloorEntity floor2 = new FloorEntity();
        floor2.setFloorNumber(1);

        when(floorsRepository.findByFloorNumber(0)).thenReturn(floor1);
        when(floorsRepository.findAll()).thenReturn(List.of(floor1, floor2));

        ResponseEntity<String> response = floorsService.deleteFloor(0);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void addRoomToFloorShouldAddRoom() {
        FloorEntity floor = new FloorEntity();
        floor.setFloorNumber(1);
        floor.setRooms(new ArrayList<>());

        when(floorsRepository.findByFloorNumber(1)).thenReturn(floor);

        floorsService.addRoomToFloor(99L, 1);

        assertThat(floor.getRooms()).contains(99L);
        verify(floorsRepository).save(floor);
    }

    @Test
    void removeRoomFromFloorShouldRemoveRoom() {
        FloorEntity floor = new FloorEntity();
        floor.setFloorNumber(1);
        floor.setRooms(new ArrayList<>(List.of(10L, 20L)));

        when(floorsRepository.findByFloorNumber(1)).thenReturn(floor);

        boolean removed = floorsService.removeRoomFromFloor(10L, 1);

        assertThat(removed).isTrue();
        assertThat(floor.getRooms()).doesNotContain(10L);
        verify(floorsRepository).save(floor);
    }

    @Test
    void removeRoomFromFloorShouldReturnFalseIfNotFound() {
        FloorEntity floor = new FloorEntity();
        floor.setFloorNumber(1);
        floor.setRooms(new ArrayList<>(List.of(20L)));

        when(floorsRepository.findByFloorNumber(1)).thenReturn(floor);

        boolean removed = floorsService.removeRoomFromFloor(10L, 1);

        assertThat(removed).isFalse();
        verify(floorsRepository, never()).save(any());
    }
}

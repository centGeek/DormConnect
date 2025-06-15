package pl.lodz.eventservice.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.entity.EventEntity;
import pl.lodz.eventservice.mapper.EventMapper;
import pl.lodz.eventservice.repository.EventRepository;
import pl.lodz.eventservice.service.EventApprovalService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventApprovalServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventApprovalService eventApprovalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("approveEvent should set status APPROVED and save entity")
    void approveEvent_shouldSetStatusApproved() {
        Long eventId = 1L;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setApprovalStatus(ApprovalStatus.WAITING);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);

        eventApprovalService.approveEvent(eventId);

        assertEquals(ApprovalStatus.APPROVED, eventEntity.getApprovalStatus());
        verify(eventRepository).save(eventEntity);
    }

    @Test
    @DisplayName("approveEvent should throw EntityNotFoundException if event not found")
    void approveEvent_shouldThrowIfNotFound() {
        Long eventId = 99L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventApprovalService.approveEvent(eventId));

        assertTrue(exception.getMessage().contains("Event not found with ID: " + eventId));
    }

    @Test
    @DisplayName("rejectEvent should set status DECLINED and save entity")
    void rejectEvent_shouldSetStatusDeclined() {
        Long eventId = 2L;
        EventEntity eventEntity = new EventEntity();
        eventEntity.setApprovalStatus(ApprovalStatus.WAITING);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);

        eventApprovalService.rejectEvent(eventId);

        assertEquals(ApprovalStatus.DECLINED, eventEntity.getApprovalStatus());
        verify(eventRepository).save(eventEntity);
    }

    @Test
    @DisplayName("rejectEvent should throw EntityNotFoundException if event not found")
    void rejectEvent_shouldThrowIfNotFound() {
        Long eventId = 100L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> eventApprovalService.rejectEvent(eventId));

        assertTrue(exception.getMessage().contains("Event not found with ID: " + eventId));
    }

    @Test
    @DisplayName("getAllEvents should return page of EventDTO")
    void getAllEvents_shouldReturnPagedDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        EventEntity eventEntity = new EventEntity();
        EventDTO eventDTO = mock(EventDTO.class);

        Page<EventEntity> pageEntity = new PageImpl<>(List.of(eventEntity), pageable, 1);

        when(eventRepository.findAllByStartDateTimeAfter(any(LocalDateTime.class), eq(pageable))).thenReturn(pageEntity);
        when(eventMapper.toEventDTO(eventEntity)).thenReturn(eventDTO);

        Page<EventDTO> result = eventApprovalService.getAllEvents(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(eventDTO, result.getContent().getFirst());

        verify(eventRepository).findAllByStartDateTimeAfter(any(LocalDateTime.class), eq(pageable));
        verify(eventMapper).toEventDTO(eventEntity);
    }

    @Test
    @DisplayName("getEventsByApprovalStatus should return page of EventDTO")
    void getEventsByApprovalStatus_shouldReturnPagedDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        ApprovalStatus status = ApprovalStatus.APPROVED;
        EventEntity eventEntity = new EventEntity();
        EventDTO eventDTO = mock(EventDTO.class);

        Page<EventEntity> pageEntity = new PageImpl<>(List.of(eventEntity), pageable, 1);

        when(eventRepository.findByApprovalStatus(status, pageable)).thenReturn(pageEntity);
        when(eventMapper.toEventDTO(eventEntity)).thenReturn(eventDTO);

        Page<EventDTO> result = eventApprovalService.getEventsByApprovalStatus(status, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(eventDTO, result.getContent().get(0));

        verify(eventRepository).findByApprovalStatus(status, pageable);
        verify(eventMapper).toEventDTO(eventEntity);
    }
}

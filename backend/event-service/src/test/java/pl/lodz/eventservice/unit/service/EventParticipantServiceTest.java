package pl.lodz.eventservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.EventEntity;
import pl.lodz.eventservice.mapper.EventMapper;
import pl.lodz.eventservice.repository.EventRepository;
import pl.lodz.eventservice.service.EventParticipantService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventParticipantServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventParticipantService participantService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private EventEntity createEventEntityWithParticipants(List<Long> participants, int maxParticipants) {
        EventEntity entity = new EventEntity();
        entity.setParticipantId(new ArrayList<>(participants));
        entity.setMaxParticipants(maxParticipants);
        return entity;
    }

    @Test
    @DisplayName("joinEvent adds participant if not already joined and capacity available")
    void joinEvent_addsParticipantSuccessfully() {
        Long eventId = 1L;
        Long participantId = 42L;
        List<Long> participants = List.of(10L, 11L);

        EventEntity event = createEventEntityWithParticipants(participants, 5);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(eventMapper.toEventDTO(any(EventEntity.class))).thenReturn(mock(EventDTO.class));

        Optional<EventDTO> result = participantService.joinEvent(eventId, participantId);

        assertTrue(result.isPresent());
        verify(eventRepository).save(event);
        assertTrue(event.getParticipantId().contains(participantId));
        assertEquals(3, event.getParticipantId().size());
    }

    @Test
    @DisplayName("joinEvent returns empty if participant already joined")
    void joinEvent_returnsEmptyIfAlreadyJoined() {
        Long eventId = 1L;
        Long participantId = 10L;
        List<Long> participants = List.of(10L, 11L);

        EventEntity event = createEventEntityWithParticipants(participants, 5);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Optional<EventDTO> result = participantService.joinEvent(eventId, participantId);

        assertTrue(result.isEmpty());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("joinEvent returns empty if max participants reached")
    void joinEvent_returnsEmptyIfMaxReached() {
        Long eventId = 1L;
        Long participantId = 42L;
        List<Long> participants = List.of(10L, 11L, 12L);

        EventEntity event = createEventEntityWithParticipants(participants, 3);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Optional<EventDTO> result = participantService.joinEvent(eventId, participantId);

        assertTrue(result.isEmpty());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("leaveEvent removes participant if joined")
    void leaveEvent_removesParticipantSuccessfully() {
        Long eventId = 1L;
        Long participantId = 10L;
        List<Long> participants = new ArrayList<>(List.of(10L, 11L));

        EventEntity event = createEventEntityWithParticipants(participants, 5);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(eventMapper.toEventDTO(any(EventEntity.class))).thenReturn(mock(EventDTO.class));

        Optional<EventDTO> result = participantService.leaveEvent(eventId, participantId);

        assertTrue(result.isPresent());
        verify(eventRepository).save(event);
        assertFalse(event.getParticipantId().contains(participantId));
        assertEquals(1, event.getParticipantId().size());
    }

    @Test
    @DisplayName("leaveEvent returns empty if participant not joined")
    void leaveEvent_returnsEmptyIfNotJoined() {
        Long eventId = 1L;
        Long participantId = 42L;
        List<Long> participants = List.of(10L, 11L);

        EventEntity event = createEventEntityWithParticipants(participants, 5);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Optional<EventDTO> result = participantService.leaveEvent(eventId, participantId);

        assertTrue(result.isEmpty());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("getAllEventsForParticipant returns page of events")
    void getAllEventsForParticipant_returnsPage() {
        Long participantId = 10L;
        Pageable pageable = PageRequest.of(0, 10);

        EventEntity event = new EventEntity();
        EventDTO dto = mock(EventDTO.class);

        Page<EventEntity> pageEntity = new PageImpl<>(List.of(event), pageable, 1);

        when(eventRepository.findByParticipantIdContaining(participantId, pageable)).thenReturn(pageEntity);
        when(eventMapper.toEventDTO(event)).thenReturn(dto);

        Page<EventDTO> result = participantService.getAllEventsForParticipant(participantId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().getFirst());
    }
}

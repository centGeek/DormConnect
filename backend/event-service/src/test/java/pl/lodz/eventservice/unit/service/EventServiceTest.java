package pl.lodz.eventservice.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.entity.EventEntity;
import pl.lodz.eventservice.mapper.EventMapper;
import pl.lodz.eventservice.repository.EventRepository;
import pl.lodz.eventservice.service.EventParticipantService;
import pl.lodz.eventservice.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        eventMapper = mock(EventMapper.class);
        eventService = new EventService(eventRepository, eventMapper);
    }

    @Test
    @DisplayName("createEvent returns Optional.empty when participants exceed max")
    void createEvent_returnsEmpty_whenParticipantsExceedMax() {
        EventCreateDTO createDTO = new EventCreateDTO(
                "Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Loc",
                "Type",
                2,
                "url",
                1L,
                List.of(1L, 2L, 3L)
        );

        // participants size 3 > maxParticipants 2
        Optional<EventDTO> result = eventService.createEvent(createDTO);
        assertTrue(result.isEmpty());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("createEvent saves event and returns DTO when valid")
    void createEvent_savesAndReturnsDTO_whenValid() {
        EventCreateDTO createDTO = new EventCreateDTO(
                "Event",
                "Desc",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Loc",
                "Type",
                5,
                "url",
                1L,
                List.of(1L, 2L)
        );

        EventEntity entity = new EventEntity();
        EventDTO dto = new EventDTO(1L, "Event", "Desc", createDTO.startDateTime(), createDTO.endDateTime(),
                createDTO.location(), createDTO.eventType(), createDTO.maxParticipants(),
                createDTO.imageUrl(), createDTO.organizerId(), ApprovalStatus.APPROVED, createDTO.participantId());

        when(eventMapper.toEntity(createDTO)).thenReturn(entity);
        when(eventRepository.save(entity)).thenReturn(entity);
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        Optional<EventDTO> result = eventService.createEvent(createDTO);

        // participants size 2 < maxParticipants 5
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(eventRepository).save(entity);
    }

    @Test
    @DisplayName("getEventById returns DTO when found")
    void getEventById_returnsDTO_whenFound() {
        EventEntity entity = new EventEntity();
        EventDTO dto = mock(EventDTO.class);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        Optional<EventDTO> result = eventService.getEventById(1L);
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    @DisplayName("getAllApprovedEvents returns page of DTOs")
    void getAllApprovedEvents_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        EventEntity entity = new EventEntity();
        EventDTO dto = mock(EventDTO.class);

        Page<EventEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(eventRepository.findAllByApprovalStatusAndStartDateTimeAfter(eq(ApprovalStatus.APPROVED), any(), eq(pageable)))
                .thenReturn(entityPage);
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        Page<EventDTO> result = eventService.getAllApprovedEvents(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().getFirst());
    }

    @Test
    @DisplayName("deleteEvent deletes event if found")
    void deleteEvent_deletes_whenFound() {
        EventEntity entity = new EventEntity();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));

        eventService.deleteEvent(1L);

        verify(eventRepository).delete(entity);
    }

    @Test
    @DisplayName("deleteEvent throws IllegalArgumentException when event not found")
    void deleteEvent_throwsException_whenNotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(99L));
        verify(eventRepository, never()).delete(any());
    }

    @Test
    @DisplayName("isOrganizer returns true when user is organizer")
    void isOrganizer_returnsTrue_whenUserIsOrganizer() {
        EventEntity entity = new EventEntity();
        entity.setOrganizerId(42L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertTrue(eventService.isOrganizer(42L, 1L));
    }

    @Test
    @DisplayName("isOrganizer returns false when user is not organizer")
    void isOrganizer_returnsFalse_whenUserIsNotOrganizer() {
        EventEntity entity = new EventEntity();
        entity.setOrganizerId(100L);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertFalse(eventService.isOrganizer(42L, 1L));
    }

    @Test
    @DisplayName("isOrganizer returns false when event not found")
    void isOrganizer_returnsFalse_whenEventNotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertFalse(eventService.isOrganizer(42L, 99L));
    }
}

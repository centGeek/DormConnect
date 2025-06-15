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
import pl.lodz.eventservice.service.EventOrganizerService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventOrganizerServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventOrganizerService eventOrganizerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getAllCreatedEvent should return paged events for organizer")
    void getAllCreatedEvent_returnsPagedEvents() {
        Long organizerId = 5L;
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();

        EventEntity eventEntity = new EventEntity();
        EventDTO eventDTO = new EventDTO(
                1L,
                "Event",
                "Desc",
                now.plusDays(1),
                now.plusDays(2),
                "Location",
                "Type",
                50,
                "imageUrl",
                organizerId,
                null,
                List.of()
        );

        Page<EventEntity> pageEntity = new PageImpl<>(List.of(eventEntity), pageable, 1);

        when(eventRepository.findByOrganizerIdAndStartDateTimeAfter(eq(organizerId), eq(pageable), any(LocalDateTime.class)))
                .thenReturn(pageEntity);
        when(eventMapper.toEventDTO(eventEntity)).thenReturn(eventDTO);

        Page<EventDTO> result = eventOrganizerService.getAllCreatedEvent(organizerId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(eventDTO, result.getContent().getFirst());

        verify(eventRepository).findByOrganizerIdAndStartDateTimeAfter(eq(organizerId), eq(pageable), any(LocalDateTime.class));
        verify(eventMapper).toEventDTO(eventEntity);
    }

    @Test
    @DisplayName("hasOrganizedEvent should return true if organizer has events")
    void hasOrganizedEvent_returnsTrueIfExists() {
        Long organizerId = 7L;

        when(eventRepository.existsByOrganizerId(organizerId)).thenReturn(true);

        boolean result = eventOrganizerService.hasOrganizedEvent(organizerId);

        assertTrue(result);
        verify(eventRepository).existsByOrganizerId(organizerId);
    }

    @Test
    @DisplayName("hasOrganizedEvent should return false if organizer has no events")
    void hasOrganizedEvent_returnsFalseIfNotExists() {
        Long organizerId = 8L;

        when(eventRepository.existsByOrganizerId(organizerId)).thenReturn(false);

        boolean result = eventOrganizerService.hasOrganizedEvent(organizerId);

        assertFalse(result);
        verify(eventRepository).existsByOrganizerId(organizerId);
    }
}

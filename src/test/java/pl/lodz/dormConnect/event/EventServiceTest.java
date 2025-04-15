package pl.lodz.dormConnect.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.model.EventEntity;
import pl.lodz.dormConnect.events.repository.EventRepository;
import pl.lodz.dormConnect.events.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private EventDTO dto;
    private EventEntity entity;

    @BeforeEach
    void setup() {
        dto = new EventDTO(null, "Test", "Desc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Loc", "PUBLIC", 10, "img.png", List.of(1L), List.of());
        entity = EventEntity.builder()
                .eventName("Test")
                .description("Desc")
                .startDateTime(dto.startDateTime())
                .endDateTime(dto.endDateTime())
                .location("Loc")
                .eventType("PUBLIC")
                .maxParticipants(10)
                .imageUrl("img.png")
                .organizerId(List.of(1L))
                .participantId(List.of())
                .build();
    }

    @Test
    void shouldCreateEvent() {
        when(eventMapper.toEntity(dto)).thenReturn(entity);
        when(eventRepository.save(entity)).thenReturn(entity);
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        EventDTO result = eventService.createEvent(dto);

        assertEquals(dto, result);
        verify(eventRepository).save(entity);
    }

    @Test
    void shouldGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        EventDTO result = eventService.getEventById(1L);

        assertEquals(dto, result);
    }

    @Test
    void shouldThrowIfEventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventService.getEventById(1L));
    }
}

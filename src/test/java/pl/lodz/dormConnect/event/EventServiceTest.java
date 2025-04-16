package pl.lodz.dormConnect.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.dormConnect.events.dto.EventCreateDTO;
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
    private EventCreateDTO dtoCreate;

    @BeforeEach
    void setup() {
        dto = new EventDTO(null, "Test", "Desc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Loc", "PUBLIC", 10, "img.png", List.of(1L), List.of());
        dtoCreate = new EventCreateDTO( "Test", "Desc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Loc", "PUBLIC", 10, "img.png", List.of(1L), List.of());
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
        when(eventMapper.toEntity(dtoCreate)).thenReturn(entity);
        when(eventRepository.save(entity)).thenReturn(entity);
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        EventDTO result = eventService.createEvent(dtoCreate);

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

    @Test
    void shouldDeleteEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));

        eventService.deleteEvent(1L);

        verify(eventRepository).delete(entity);
    }


    @Test
    void shouldThrowWhenDeletingNonexistentEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(1L));
    }


    @Test
    void shouldUpdateEvent() {
        EventDTO updatedDto = new EventDTO(1L, "Updated", "New Desc", dto.startDateTime(), dto.endDateTime(), "New Loc", "PRIVATE", 20, "new.png", List.of(2L), List.of());
        EventEntity updatedEntity = EventEntity.builder()
                .eventId(1L)
                .eventName("Updated")
                .description("New Desc")
                .startDateTime(dto.startDateTime())
                .endDateTime(dto.endDateTime())
                .location("New Loc")
                .eventType("PRIVATE")
                .maxParticipants(20)
                .imageUrl("new.png")
                .organizerId(List.of(2L))
                .participantId(List.of())
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(updatedEntity));

        when(eventRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(eventMapper.toEventDTO(updatedEntity)).thenReturn(updatedDto);

        EventDTO result = eventService.updateEvent(1L, updatedDto);

        assertEquals(updatedDto, result);
    }


    @Test
    void shouldReturnAllEvents() {

        when(eventRepository.findAll()).thenReturn(List.of(entity));
        when(eventMapper.toEventDTOList(List.of(entity))).thenReturn(List.of(dto));

        List<EventDTO> results = eventService.getAllEvents();

        assertEquals(1, results.size());
        assertEquals(dto, results.getFirst());
    }

}

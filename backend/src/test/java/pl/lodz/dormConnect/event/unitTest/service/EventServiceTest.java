package pl.lodz.dormConnect.event.unitTest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private EventCreateDTO notCreated;

    @BeforeEach
    void setup() {
        dto = new EventDTO(null, "Test", "Desc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Loc", "PUBLIC", 10, "img.png", 1L, false, List.of());
        dtoCreate = new EventCreateDTO( "Test", "Desc", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Loc", "PUBLIC", 10, "img.png", 1L, List.of(2L, 3L));
        entity = EventEntity.builder()
                .eventName("Test")
                .description("Desc")
                .startDateTime(dto.startDateTime())
                .endDateTime(dto.endDateTime())
                .location("Loc")
                .eventType("PUBLIC")
                .maxParticipants(10)
                .imageUrl("img.png")
                .organizerId(1L)
                .participantId(List.of())
                .build();

        notCreated = new EventCreateDTO(
                "Test",
                "Desc",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                "Loc",
                "PUBLIC",
                1,
                "img.png",
                1L,
                List.of(1L, 2L, 3L)
        );
    }

    @Test
    void shouldCreateEvent() {
        when(eventMapper.toEntity(dtoCreate)).thenReturn(entity);
        when(eventRepository.save(entity)).thenReturn(entity);
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        Optional<EventDTO> result = eventService.createEvent(dtoCreate);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(eventRepository).save(entity);
    }

    @Test
    void shouldNotCreateEvent() {
        Optional<EventDTO> result = eventService.createEvent(notCreated);

        assertFalse(result.isPresent());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldGetEventById() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        Optional<EventDTO> result = eventService.getEventById(1L);
        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void shouldReturnEmptyOptionalIfEventNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<EventDTO> result = eventService.getEventById(1L);

        assertFalse(result.isPresent());
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
        EventDTO updatedDto = new EventDTO(1L, "Updated", "New Desc", dto.startDateTime(), dto.endDateTime(), "New Loc", "PRIVATE", 20, "new.png", 2L, false, List.of());
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
                .organizerId(2L)
                .participantId(List.of())
                .build();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(updatedEntity));

        when(eventRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(eventMapper.toEventDTO(updatedEntity)).thenReturn(updatedDto);

        Optional<EventDTO> result = eventService.updateEvent(1L, updatedDto);

        assertTrue(result.isPresent());
        assertEquals(updatedDto, result.get());
    }


    @Test
    void shouldReturnPagedEvents() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventEntity> pageOfEntities = new PageImpl<>(List.of(entity));
        Page<EventDTO> pageOfDtos = new PageImpl<>(List.of(dto));

        when(eventRepository.findAll(pageable)).thenReturn(pageOfEntities);
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        // when
        Page<EventDTO> results = eventService.getAllEvents(pageable);

        // then
        assertEquals(1, results.getTotalElements());
        assertEquals(dto, results.getContent().get(0));
    }


}

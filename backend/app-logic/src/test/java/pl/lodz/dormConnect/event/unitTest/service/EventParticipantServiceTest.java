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
import pl.lodz.dormConnect.events.repository.EventRepository;
import pl.lodz.dormConnect.events.service.EventParticipantService;
import pl.lodz.EventEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventParticipantServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventParticipantService eventParticipantService;

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
    void shouldReturnPagedEventsForParticipant() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EventEntity> pageOfEntities = new PageImpl<>(List.of(entity));
        Page<EventDTO> pageOfDtos = new PageImpl<>(List.of(dto));

        when(eventRepository.findByParticipantIdContaining(1L, pageable)).thenReturn(pageOfEntities);
        when(eventMapper.toEventDTO(entity)).thenReturn(dto);

        Page<EventDTO> results = eventParticipantService.getAllEventsForParticipant(1L, pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals(dto, results.getContent().get(0));
    }

    @Test
    void shouldJoinEventParticipant() {

    }

    @Test
    void shouldLeaveEventParticipant() {

    }
}

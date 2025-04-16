package pl.lodz.dormConnect.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.events.dto.EventCreateDTO;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.model.EventEntity;
import pl.lodz.dormConnect.events.repository.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public EventDTO createEvent(EventCreateDTO eventCreateDTO) {
        EventEntity savedEvent = eventRepository.save(eventMapper.toEntity(eventCreateDTO));
        return eventMapper.toEventDTO(savedEvent);
    }

    public Optional<EventDTO> getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .map(eventMapper::toEventDTO);
    }

    public List<EventDTO> getAllEvents() {
        List<EventEntity> events = eventRepository.findAll();
        return eventMapper.toEventDTOList(events);
    }

    public Optional<EventDTO> updateEvent(Long eventId, EventDTO eventDTO) {
        return eventRepository.findById(eventId).map(eventEntity -> {
            eventEntity.setEventName(eventDTO.eventName());
            eventEntity.setDescription(eventDTO.description());
            eventEntity.setStartDateTime(eventDTO.startDateTime());
            eventEntity.setEndDateTime(eventDTO.endDateTime());
            eventEntity.setLocation(eventDTO.location());
            eventEntity.setMaxParticipants(eventDTO.maxParticipants());
            eventEntity.setImageUrl(eventDTO.imageUrl());
            eventEntity.setOrganizerId(eventDTO.organizerId());
            eventEntity.setParticipantId(eventDTO.participantId());

            EventEntity updatedEvent = eventRepository.save(eventEntity);
            return eventMapper.toEventDTO(updatedEvent);
        });
    }

    public void deleteEvent(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(eventEntity);
    }

}

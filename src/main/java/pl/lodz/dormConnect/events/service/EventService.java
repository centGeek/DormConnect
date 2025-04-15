package pl.lodz.dormConnect.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.model.EventEntity;
import pl.lodz.dormConnect.events.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        EventEntity eventEntity = eventMapper.mapToEntity(eventDTO);
        EventEntity savedEvent = eventRepository.save(eventEntity);
        return eventMapper.mapToDTO(savedEvent);
    }

    public EventDTO getEventById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return eventMapper.mapToDTO(eventEntity);
    }

    public List<EventDTO> getAllEvents() {
        List<EventEntity> events = eventRepository.findAll();
        return events.stream()
                .map(this.eventMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteEvent(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(eventEntity);
    }

}

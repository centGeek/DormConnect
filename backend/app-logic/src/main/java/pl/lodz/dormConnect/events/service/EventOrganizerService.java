package pl.lodz.dormConnect.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.repository.EventRepository;
import pl.lodz.entity.EventEntity;

@Service
public class EventOrganizerService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventOrganizerService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> getAllCreatedEvent(Long organizerId, Pageable pageable) {
        Page<EventEntity> page = eventRepository.findByOrganizerId(organizerId, pageable);
        return page.map(eventMapper::toEventDTO);
    }

    public boolean hasOrganizedEvent(Long organizerId) {
        // Zamiast liczyć wszystkie wydarzenia, sprawdzamy, czy istnieje jakiekolwiek
        return eventRepository.existsByOrganizerId(organizerId);
    }

}

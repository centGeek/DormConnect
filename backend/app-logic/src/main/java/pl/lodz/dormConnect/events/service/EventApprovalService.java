package pl.lodz.dormConnect.events.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.repository.EventRepository;
import pl.lodz.EventEntity;

@Service
public class EventApprovalService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventApprovalService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional
    public void approveEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));

        event.setIsApproved(true);
        eventRepository.save(event);
    }

    @Transactional
    public void rejectEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));

        event.setIsApproved(false);
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEvents(Pageable pageable) {
        Page<EventEntity> page = eventRepository.findAll(pageable);
        return page.map(eventMapper::toEventDTO);
    }
}

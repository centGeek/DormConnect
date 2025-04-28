package pl.lodz.dormConnect.events.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.dormConnect.events.model.EventEntity;
import pl.lodz.dormConnect.events.repository.EventRepository;

@Service
public class EventApprovalService {

    private final EventRepository eventRepository;

    @Autowired
    public EventApprovalService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
}

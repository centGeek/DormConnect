package pl.lodz.eventservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.entity.EventEntity;
import pl.lodz.eventservice.mapper.EventMapper;
import pl.lodz.eventservice.repository.EventRepository;

import java.time.LocalDateTime;

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

        event.setApprovalStatus(ApprovalStatus.APPROVED);
        eventRepository.save(event);
    }

    @Transactional
    public void rejectEvent(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));

        event.setApprovalStatus(ApprovalStatus.DECLINED);
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEvents(Pageable pageable) {
        Page<EventEntity> page = eventRepository.findAllByStartDateTimeAfter(LocalDateTime.now(), pageable);
        return page.map(eventMapper::toEventDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> getEventsByApprovalStatus(ApprovalStatus status, Pageable pageable) {
        return eventRepository.findByApprovalStatus(status, pageable).map(eventMapper::toEventDTO);
    }
}

package pl.lodz.eventservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.eventservice.dto.EventCreateDTO;
import pl.lodz.eventservice.dto.EventDTO;
import pl.lodz.eventservice.entity.ApprovalStatus;
import pl.lodz.eventservice.entity.EventEntity;
import pl.lodz.eventservice.mapper.EventMapper;
import pl.lodz.eventservice.repository.EventRepository;

import java.time.LocalDateTime;
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

    @Transactional
    public Optional<EventDTO> createEvent(EventCreateDTO eventCreateDTO) {
        if (eventCreateDTO.participantId().size() >= eventCreateDTO.maxParticipants()) {
            return Optional.empty();
        }
        EventEntity savedEvent = eventRepository.save(eventMapper.toEntity(eventCreateDTO));
        return Optional.of(eventMapper.toEventDTO(savedEvent));
    }

    @Transactional(readOnly = true)
    public Optional<EventDTO> getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .map(eventMapper::toEventDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> getAllApprovedEvents(Pageable pageable) {
        Page<EventEntity> page = eventRepository.findAllByApprovalStatusAndStartDateTimeAfter(ApprovalStatus.APPROVED, LocalDateTime.now(), pageable);
        return page.map(eventMapper::toEventDTO);
    }

    @Transactional
    public Optional<EventDTO> updateEvent(Long eventId, EventDTO eventDTO) {

        List<Long> participantIds = Optional.ofNullable(eventDTO.participantId()).orElse(List.of());

        if (participantIds.size() >= eventDTO.maxParticipants()) {
            return Optional.empty();
        }

        return eventRepository.findById(eventId).map(eventEntity -> {
            eventEntity.setEventName(eventDTO.eventName());
            eventEntity.setDescription(eventDTO.description());
            eventEntity.setStartDateTime(eventDTO.startDateTime());
            eventEntity.setEndDateTime(eventDTO.endDateTime());
            eventEntity.setLocation(eventDTO.location());
            eventEntity.setMaxParticipants(eventDTO.maxParticipants());
            eventEntity.setImageUrl(eventDTO.imageUrl());
            eventEntity.setOrganizerId(eventDTO.organizerId());

            eventEntity.setEventType(eventDTO.eventType());
            eventEntity.setApprovalStatus(eventDTO.approvalStatus());

            EventEntity updatedEvent = eventRepository.save(eventEntity);
            return eventMapper.toEventDTO(updatedEvent);
        });
    }


    @Transactional
    public void deleteEvent(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        eventRepository.delete(eventEntity);
    }

    public boolean isOrganizer(Long userId, Long eventId) {
        Optional<EventEntity> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            EventEntity event = eventOptional.get();
            return event.getOrganizerId().equals(userId);
        }

        return false;
    }

}

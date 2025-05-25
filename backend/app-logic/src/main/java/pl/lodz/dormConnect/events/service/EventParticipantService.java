package pl.lodz.dormConnect.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.commons.entity.EventEntity;
import pl.lodz.commons.repository.jpa.EventRepository;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventParticipantService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventParticipantService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional
    public Optional<EventDTO> joinEvent(Long eventId, Long participantId) {
        return eventRepository.findById(eventId)
                .filter(event -> !event.getParticipantId().contains(participantId))
                .filter(event -> event.getParticipantId().size() < event.getMaxParticipants())
                .map(event -> {
                    List<Long> participants = new ArrayList<>(event.getParticipantId());
                    participants.add(participantId);
                    event.setParticipantId(participants);
                    EventEntity savedEvent = eventRepository.save(event);
                    return eventMapper.toEventDTO(savedEvent);
                });
    }

    @Transactional
    public Optional<EventDTO> leaveEvent(Long eventId, Long participantId) {
        return eventRepository.findById(eventId)
                .filter(event -> event.getParticipantId().contains(participantId))
                .map(event -> {
                    List<Long> participants = new ArrayList<>(event.getParticipantId());
                    participants.remove(participantId);
                    event.setParticipantId(participants);
                    EventEntity savedEvent = eventRepository.save(event);
                    return eventMapper.toEventDTO(savedEvent);
                });
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> getAllEventsForParticipant(Long participantId, Pageable pageable) {
        Page<EventEntity> page = eventRepository.findByParticipantIdContaining(participantId, pageable);
        return page.map(eventMapper::toEventDTO);
    }

}

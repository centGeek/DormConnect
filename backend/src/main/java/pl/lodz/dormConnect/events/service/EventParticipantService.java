package pl.lodz.dormConnect.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.mapper.EventMapper;
import pl.lodz.dormConnect.events.model.EventEntity;
import pl.lodz.dormConnect.events.repository.EventParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventParticipantService {

    private final EventParticipantRepository eventParticipantRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventParticipantService(EventParticipantRepository eventParticipantRepository, EventMapper eventMapper) {
        this.eventParticipantRepository = eventParticipantRepository;
        this.eventMapper = eventMapper;
    }


    public Optional<EventDTO> joinEvent(Long eventId, Long participantId) {
        return eventParticipantRepository.findById(eventId)
                .filter(event -> !event.getParticipantId().contains(participantId))
                .filter(event -> event.getParticipantId().size() < event.getMaxParticipants())
                .map(event -> {
                    List<Long> participants = new ArrayList<>(event.getParticipantId());
                    participants.add(participantId);
                    event.setParticipantId(participants);
                    EventEntity savedEvent = eventParticipantRepository.save(event);
                    return eventMapper.toEventDTO(savedEvent);
                });
    }

    public Optional<EventDTO> leaveEvent(Long eventId, Long participantId) {
        return eventParticipantRepository.findById(eventId)
                .filter(event -> event.getParticipantId().contains(participantId))
                .map(event -> {
                    List<Long> participants = new ArrayList<>(event.getParticipantId());
                    participants.remove(participantId);
                    event.setParticipantId(participants);
                    EventEntity savedEvent = eventParticipantRepository.save(event);
                    return eventMapper.toEventDTO(savedEvent);
                });
    }

}

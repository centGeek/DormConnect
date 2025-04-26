package pl.lodz.dormConnect.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.events.dto.EventDTO;
import pl.lodz.dormConnect.events.repository.EventParticipantRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventPaticipantService {

    private final EventParticipantRepository eventParticipantRepository;

    @Autowired
    public EventPaticipantService(EventParticipantRepository eventParticipantRepository) {
        this.eventParticipantRepository = eventParticipantRepository;
    }


    public void leaveEvent(Long eventId, Long participantId) {
    }

    public void joinEvent(Long eventId, Long participantId) {
    }

    public List<EventDTO> getAllEvents(Long participantId) {

        return new ArrayList<>();
    }
}

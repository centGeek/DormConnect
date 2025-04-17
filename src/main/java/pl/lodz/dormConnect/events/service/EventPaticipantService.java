package pl.lodz.dormConnect.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.events.repository.EventParticipantRepository;

@Service
public class EventPaticipantService {

    private final EventParticipantRepository eventParticipantRepository;

    @Autowired
    public EventPaticipantService(EventParticipantRepository eventParticipantRepository) {
        this.eventParticipantRepository = eventParticipantRepository;
    }

}

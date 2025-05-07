package pl.lodz.dormConnect.schedule.service;

import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.schedule.repositories.CommonRoomAssigmentRepository;

@Service
class CommonRoomAssgimentService {
    private final CommonRoomAssigmentRepository repository;

    public CommonRoomAssgimentService(CommonRoomAssigmentRepository repository) {
        this.repository = repository;
    }

    // Dodaj metody korzystające z repository
}
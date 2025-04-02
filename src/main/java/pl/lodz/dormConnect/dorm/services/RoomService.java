package pl.lodz.dormConnect.dorm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.dorm.repositories.RoomRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    public List<RoomEntity> getAllRooms(){
        return StreamSupport.stream(roomRepository.findAll().spliterator(),false).toList();
    }

    public RoomEntity addRoom(RoomEntity roomEntity){
        return roomRepository.save(roomEntity);
    }

}

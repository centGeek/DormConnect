package pl.lodz.dormConnect.dorm.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dorm.entities.RoomAssignEntity;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.dorm.repositories.RoomAssigmentRepository;
import pl.lodz.dormConnect.dorm.repositories.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final RoomAssigmentRepository roomAssigmentRepository;
    @Autowired
    public RoomService(RoomRepository roomRepository, RoomAssigmentRepository roomAssigmentRepository) {
        this.roomRepository = roomRepository;
        this.roomAssigmentRepository = roomAssigmentRepository;
    }


    public List<RoomEntity> getAllRooms(){
        return StreamSupport.stream(roomRepository.findAll().spliterator(),false).toList();
    }

    public RoomEntity addRoom(RoomEntity roomEntity){
        return roomRepository.save(roomEntity);
    }

    public Optional<RoomEntity> findById(Long id){
        return roomRepository.findById(id);
    }

    public boolean deleteRoomByEntity(RoomEntity roomEntity){
        roomRepository.delete(roomEntity);
        return true;
    }

    public boolean deleteRoomById(Long id){
        roomRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean assignStudentToRoom(RoomEntity roomEntity, Long studentId, LocalDate fromDate,LocalDate toDate){
        if (roomAssigmentRepository.countActiveAssignmentsForRoom(roomEntity.getId(),fromDate,toDate) > roomEntity.getCapacity()) {
            return false;
        }
        else if (roomAssigmentRepository.existsAssignmentForStudentDuring(studentId,fromDate,toDate)){
            return false;
        }

        RoomAssignEntity roomAssignEntity = new RoomAssignEntity();
        roomAssignEntity.setRoom(roomEntity);
        roomAssignEntity.setResidentId(studentId);
        roomAssignEntity.setFromDate(fromDate);
        roomAssignEntity.setToDate(toDate);


        return true;
    }
}

package pl.lodz.dormConnect.dorm.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.dormConnect.dorm.DTO.AssignmentsDTO;
import pl.lodz.dormConnect.dorm.repositories.RoomAssignmentRepository;
import pl.lodz.dormConnect.dorm.repositories.RoomRepository;
import pl.lodz.entity.RoomAssignEntity;
import pl.lodz.entity.RoomEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final RoomAssignmentRepository roomAssignmentRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomAssignmentRepository roomAssignmentRepository) {
        this.roomRepository = roomRepository;
        this.roomAssignmentRepository = roomAssignmentRepository;
    }


    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll().stream().toList();
    }

    public RoomEntity addRoom(RoomEntity roomEntity) {
        return roomRepository.save(roomEntity);
    }

    public Optional<RoomEntity> findById(Long id) {
        return roomRepository.findById(id);
    }

    public boolean deleteRoomByEntity(RoomEntity roomEntity) {
        roomRepository.delete(roomEntity);
        return true;
    }

    public boolean deleteRoomById(Long id) {
        roomRepository.deleteById(id);
        return true;
    }

    @Transactional
    public RoomEntity updateRoomPartial(Long id, String newName, Integer newFloor) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        if (newName != null) {
            room.setNumber(newName);
        }
        if (newFloor != null) {
            room.setFloor(newFloor);
        }
        return roomRepository.save(room);
    }

    @Transactional
    public boolean assignStudentToRoom(RoomEntity roomEntity, Long studentId, LocalDate fromDate, LocalDate toDate) {
        //LocalDate.MAX produces an Exception on the db side.
        LocalDate toDataTest = toDate != null ? toDate : LocalDate.of(2999, 12, 31);
        if (roomAssignmentRepository.existsAssignmentForStudentDuring(studentId, fromDate, toDataTest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student already has accommodation in the given period.");
        }
        if (!canAssignNewResident(roomEntity.getId(), fromDate, toDataTest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is full at some point of the given period.");
        }
        RoomAssignEntity roomAssignEntity = new RoomAssignEntity();
        roomAssignEntity.setRoom(roomEntity);
        roomAssignEntity.setResidentId(studentId);
        roomAssignEntity.setFromDate(fromDate);
        roomAssignEntity.setToDate(toDate);
        roomAssignmentRepository.save(roomAssignEntity);
        return true;
    }


    public boolean canAssignNewResident(Long roomId, LocalDate newStart, LocalDate newEnd) {
        List<RoomAssignEntity> assignments = roomAssignmentRepository.findAssignmentsForRoomInPeriod(roomId, newStart, newEnd);
        int capacity = roomRepository.findCapacityById(roomId);

        // Mapujemy ile osób jest w każdym dniu
        Map<LocalDate, Integer> occupancy = new HashMap<>();

        for (RoomAssignEntity assignment : assignments) {
            LocalDate from = assignment.getFromDate();
            LocalDate to = assignment.getToDate() != null ? assignment.getToDate() : LocalDate.MAX;

            LocalDate effectiveStart = from.isAfter(newStart) ? from : newStart;
            LocalDate effectiveEnd = to.isBefore(newEnd) ? to : newEnd;

            for (LocalDate date = effectiveStart; !date.isAfter(effectiveEnd); date = date.plusDays(1)) {
                occupancy.put(date, occupancy.getOrDefault(date, 0) + 1);
            }
        }

        // Teraz dorzucamy symulowanego nowego mieszkańca
        LocalDate newEffectiveEnd = newEnd;
        for (LocalDate date = newStart; !date.isAfter(newEffectiveEnd); date = date.plusDays(1)) {
            int current = occupancy.getOrDefault(date, 0);
            if (current + 1 > capacity) {
                return false; // Przepełnienie!
            }
        }

        return true; // OK, można dodać
    }

    public List<AssignmentsDTO> getAssignmentsByUserId(Long userId) {
        List<RoomAssignEntity> list = roomAssignmentRepository.findAllAssignmentsByStudentId(userId);
        return list.stream()
                .map(assignment -> new AssignmentsDTO(
                        assignment.getId(),
                        userId,
                        null //TODO Pull data from another service using REST.
                        ,
                        assignment.getRoom().getNumber(),
                        assignment.getRoom().getFloor(),
                        assignment.getFromDate(),
                        assignment.getToDate()))
                .toList();
    }
}

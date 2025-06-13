package pl.lodz.dormConnect.dorm.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.dormConnect.dorm.DTO.AssignmentsDTO;
import pl.lodz.commons.entity.RoomAssignEntity;
import pl.lodz.commons.entity.RoomEntity;
import pl.lodz.commons.repository.jpa.RoomAssignmentRepository;
import pl.lodz.commons.repository.jpa.RoomRepository;
import pl.lodz.dormConnect.dorm.DTO.DeleteRoomImpactPreviewDTO;
import pl.lodz.dormConnect.dorm.DTO.ResidentReassignmentPreview;
import pl.lodz.dormConnect.dorm.DTO.SimulatedRollback;
import pl.lodz.dormConnect.dorm.scheduler.RoomAssignmentScheduler;
import pl.lodz.dormConnect.floors.service.FloorsService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;
    @Autowired
    private final RoomAssignmentRepository roomAssignmentRepository;
    private final FloorsService floorsService;
    private final RoomAssignmentScheduler roomAssignmentScheduler;

    public RoomService(RoomRepository roomRepository, RoomAssignmentRepository roomAssignmentRepository, @Lazy FloorsService floorsService, RoomAssignmentScheduler roomAssignmentScheduler) {
        this.roomRepository = roomRepository;
        this.roomAssignmentRepository = roomAssignmentRepository;
        this.floorsService = floorsService;
        this.roomAssignmentScheduler = roomAssignmentScheduler;
    }


    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll().stream().toList();
    }

    public RoomEntity addRoom(RoomEntity roomEntity) {
        RoomEntity room = roomRepository.save(roomEntity);
        floorsService.addRoomToFloor(room.getId(), room.getFloor());
        return room;
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
                        assignment.getToDate() != null && assignment.getToDate().equals(LocalDate.of(2999, 1, 1))
                                ? null
                                : assignment.getToDate()
                ))
                .toList();
    }

    public List<Integer> getFloors() {
        return roomRepository.findAll().stream()
                .map(RoomEntity::getFloor)
                .distinct()
                .toList();
    }

    public List<RoomEntity> getRoomsByFloor(int floor) {
        return roomRepository.findByFloor(floor);
    }

    public boolean simulateAssignWithoutOverflow(List<RoomAssignEntity> existingAssignments, Long roomId, LocalDate newStart, LocalDate newEnd) {
        int capacity = roomRepository.findCapacityById(roomId);
        Map<LocalDate, Integer> occupancy = new HashMap<>();

        for (RoomAssignEntity assignment : existingAssignments) {
            LocalDate from = assignment.getFromDate();
            LocalDate to = assignment.getToDate() != null ? assignment.getToDate() : LocalDate.MAX;

            LocalDate effectiveStart = from.isAfter(newStart) ? from : newStart;
            LocalDate effectiveEnd = to.isBefore(newEnd) ? to : newEnd;

            for (LocalDate date = effectiveStart; !date.isAfter(effectiveEnd); date = date.plusDays(1)) {
                occupancy.put(date, occupancy.getOrDefault(date, 0) + 1);
            }
        }

        for (LocalDate date = newStart; !date.isAfter(newEnd); date = date.plusDays(1)) {
            int current = occupancy.getOrDefault(date, 0);
            if (current + 1 > capacity) {
                return false;
            }
        }

        return true;
    }


    @Transactional
    public void shortenAssignmentEndDate(Long assignmentId, Long userId, LocalDate newEndDate) {
        RoomAssignEntity assignment = roomAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found"));

        if (!assignment.getResidentId().equals(userId)) {
            throw new IllegalArgumentException("You are not the owner of this assignment.");
        }

        LocalDate currentEnd = assignment.getToDate();
        if (currentEnd == null || currentEnd.isAfter(LocalDate.now()) == false) {
            throw new IllegalArgumentException("Assignment has already ended.");
        }

        if (newEndDate.isAfter(currentEnd)) {
            throw new IllegalArgumentException("New end date must be before current end date.");
        }

        if (newEndDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("New end date must be today or in the future.");
        }

        if (newEndDate.isAfter(LocalDate.now().plusWeeks(10))) {
            throw new IllegalArgumentException("New end date is too far in the future (max 10 weeks).");
        }

        assignment.setToDate(newEndDate);
        roomAssignmentRepository.save(assignment);
    }


    public DeleteRoomImpactPreviewDTO simulateRoomDeletionImpact(Long roomId) {
        try {
            roomAssignmentScheduler.simulateRoomDeletion(roomId);
        } catch (SimulatedRollback rollback) {
            return rollback.getPreviewDTO(); // zwracamy "wynik" symulacji
        }
        return null;
    }



    @Transactional
    public void deleteRoomWithRelocations(Long roomId) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Pobierz przypisania aktualne i przyszłe do tego pokoju
        List<RoomAssignEntity> assignmentsToReassign = roomAssignmentRepository.findByRoomIdAndToDateAfter(roomId, LocalDate.now());

        if (!assignmentsToReassign.isEmpty()) {
            // Uruchom algorytm relokacji z wysokim priorytetem dla tych przypisań
            roomAssignmentScheduler.reassignResidentsImmediately(assignmentsToReassign, roomRepository.findAll());
        }

        // Usuń wszystkie przypisania do tego pokoju (bo pokój będzie usuwany)
        //roomAssignmentRepository.deleteAll(assignmentsToReassign);

        // Usuń pokój
        roomRepository.delete(room);
    }


}

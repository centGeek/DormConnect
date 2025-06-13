package pl.lodz.dormConnect.dorm.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.commons.entity.DormFormEntity;
import pl.lodz.commons.entity.RoomAssignEntity;
import pl.lodz.commons.entity.RoomEntity;
import pl.lodz.commons.repository.jpa.DormFormRepository;
import pl.lodz.commons.repository.jpa.RoomAssignmentRepository;
import pl.lodz.commons.repository.jpa.RoomRepository;
import pl.lodz.dormConnect.dorm.DTO.ResidentReassignmentPreview;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomAssignmentScheduler {

    private final DormFormRepository dormFormRepository;
    private final RoomRepository roomRepository;
    private final RoomAssignmentRepository roomAssignRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoomAssignmentScheduler.class);

    @Transactional
       @Scheduled(fixedRate = 60000) // co minutę
    public void assignRooms() {

        RoomAssignmentLock.LOCK.lock(); // czekaj na swoją kolej

        try {
            List<DormFormEntity> forms = dormFormRepository.findByIsProcessedFalseOrderByPriorityScoreDesc();
            List<RoomEntity> allRooms = roomRepository.findAll();

            for (DormFormEntity form : forms) {
                Optional<RoomEntity> availableRoom = allRooms.stream()
                        .filter(room -> isRoomAvailable(room, form.getStartDate(), form.getEndDate()))
                        .findFirst();

                if (availableRoom.isPresent()) {
                    RoomAssignEntity assignment = new RoomAssignEntity();
                    assignment.setResidentId(form.getUserId());
                    assignment.setRoom(availableRoom.get());
                    assignment.setFromDate(form.getStartDate());
                    assignment.setToDate(form.getEndDate());

                    roomAssignRepository.save(assignment);
                    form.setProcessed(true);
                    dormFormRepository.save(form);
                } else {
                    // Brak dostępnych pokoi, można dodać logowanie lub wysyłkę powiadomień
                }
            }
        } finally {
            RoomAssignmentLock.LOCK.unlock();
        }


    }

    public List<ResidentReassignmentPreview> simulateRelocation(List<RoomAssignEntity> assignmentsToReassign, List<RoomEntity> allRooms) {
        RoomAssignmentLock.LOCK.lock(); // czekaj na swoją kolej

        try {

            List<ResidentReassignmentPreview> previews = new ArrayList<>();

            for (RoomAssignEntity assign : assignmentsToReassign) {
                Optional<RoomEntity> replacementRoom = allRooms.stream()
                        .filter(room -> !room.getId().equals(assign.getRoom().getId())) // pomijamy ten usuwany
                        .filter(room -> isRoomAvailable(room, assign.getFromDate(), assign.getToDate()))
                        .findFirst();

                if (replacementRoom.isPresent()) {
                    previews.add(new ResidentReassignmentPreview(
                            assign.getResidentId(),
                            "N/A", // można dodać `userRepository` i pobrać nazwisko
                            assign.getFromDate(),
                            assign.getToDate(),
                            assign.getFromDate(), // załóżmy, że od tego samego dnia
                            true
                    ));
                } else {
                    previews.add(new ResidentReassignmentPreview(
                            assign.getResidentId(),
                            "N/A",
                            assign.getFromDate(),
                            assign.getToDate(),
                            null,
                            false
                    ));
                }
            }

            return previews;
        } finally {
            RoomAssignmentLock.LOCK.unlock();
        }
    }

    @Transactional
    public void reassignResidentsImmediately(List<RoomAssignEntity> assignmentsToReassign, List<RoomEntity> allRooms) {
        RoomAssignmentLock.LOCK.lock();
        try {
            for (RoomAssignEntity assign : assignmentsToReassign) {
                Optional<RoomEntity> replacementRoom = allRooms.stream()
                        .filter(room -> !room.getId().equals(assign.getRoom().getId())) // pomijamy pokój, który chcemy usunąć
                        .filter(room -> isRoomAvailable(room, assign.getFromDate(), assign.getToDate()))
                        .findFirst();

                if (replacementRoom.isPresent()) {
                    // Przypisz nowy pokój
                    assign.setRoom(replacementRoom.get());
                    // Zapisz zmiany w bazie
                    roomAssignRepository.save(assign);
                } else {
                    // Jeśli nie da się przenieść, można np. logować lub rzucić wyjątek, albo po prostu pominąć
                    // Tutaj pomijamy i zostawiamy przypisanie bez zmian (lub dodaj log)
                    logger.warn("Brak dostępnego pokoju dla mieszkańca ID: " + assign.getResidentId());
                }
            }
        } finally {
            RoomAssignmentLock.LOCK.unlock();
        }
    }



    private boolean isRoomAvailable(RoomEntity room, LocalDate start, LocalDate end) {
        long currentOccupancy = room.getRoomAssigns().stream()
                .filter(assign -> overlaps(assign.getFromDate(), assign.getToDate(), start, end))
                .count();
        return currentOccupancy < room.getCapacity();
    }

    private boolean overlaps(LocalDate aStart, LocalDate aEnd, LocalDate bStart, LocalDate bEnd) {
        LocalDate aE = aEnd != null ? aEnd : LocalDate.MAX;
        LocalDate bE = bEnd != null ? bEnd : LocalDate.MAX;
        return !(aE.isBefore(bStart) || bE.isBefore(aStart));
    }
}

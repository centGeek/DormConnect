package pl.lodz.dormitoryservice.dorm.scheduler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import pl.lodz.dormitoryservice.dorm.DTO.DeleteRoomImpactPreviewDTO;
import pl.lodz.dormitoryservice.dorm.DTO.ResidentReassignmentPreview;
import pl.lodz.dormitoryservice.dorm.DTO.SimulatedRollback;
import pl.lodz.dormitoryservice.entity.DormFormEntity;
import pl.lodz.dormitoryservice.entity.RoomAssignEntity;
import pl.lodz.dormitoryservice.entity.RoomEntity;
import pl.lodz.dormitoryservice.repository.DormFormRepository;
import pl.lodz.dormitoryservice.repository.RoomAssignmentRepository;
import pl.lodz.dormitoryservice.repository.RoomRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

        LocalDate today = LocalDate.now();


        try {
            List<DormFormEntity> forms = dormFormRepository.findByIsProcessedFalseOrderByPriorityScoreDesc();
            List<RoomEntity> allRooms = roomRepository.findAll();

            forms = forms.stream().filter(form -> {
                        if (form.getEndDate() != null && form.getEndDate().isBefore(today)) {
                            dormFormRepository.delete(form);
                            return false;
                        }
                        LocalDate effectiveStart = form.getStartDate() != null && form.getStartDate().isAfter(today)
                                ? form.getStartDate()
                                : today;
                        return form.getEndDate() == null || !form.getEndDate().isBefore(effectiveStart);
                    })
                    .toList();

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
                    logger.warn("No more space for student from " + form.getStartDate() + " to " + form.getEndDate());
                }
            }
        } finally {
            RoomAssignmentLock.LOCK.unlock();
        }


    }


    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public void simulateRoomDeletion(Long roomId) {

        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        LocalDate today = LocalDate.now();
        List<RoomAssignEntity> current = room.getRoomAssigns().stream()
                .filter(a -> !a.getFromDate().isAfter(today) && !a.getToDate().isBefore(today))
                .toList();

        List<RoomAssignEntity> future = room.getRoomAssigns().stream()
                .filter(a -> a.getFromDate().isAfter(today))
                .toList();

        List<RoomEntity> otherRooms = roomRepository.findAll().stream()
                .filter(r -> !r.getId().equals(roomId))
                .toList();

        List<ResidentReassignmentPreview> nowResidents = simulateRelocation(current, otherRooms, true);
        List<ResidentReassignmentPreview> futureResidents = simulateRelocation(future, otherRooms, false);

        LocalDate minDate = Stream.concat(nowResidents.stream(), futureResidents.stream())
                .filter(ResidentReassignmentPreview::roomAvailable)
                .map(ResidentReassignmentPreview::plannedNewStartDate)
                .min(LocalDate::compareTo)
                .orElse(null);

        boolean canDeleteNow = Stream.concat(nowResidents.stream(), futureResidents.stream())
                .allMatch(ResidentReassignmentPreview::roomAvailable);

        //boolean canDeleteNow = nowResidents.stream().allMatch(ResidentReassignmentPreview::roomAvailable);

        throw new SimulatedRollback(new DeleteRoomImpactPreviewDTO(roomId, canDeleteNow, minDate, nowResidents, futureResidents));
    }

    /**
     * Use with caution as this truly creates new Assignments!
     **/
    public List<ResidentReassignmentPreview> simulateRelocation(List<RoomAssignEntity> assignmentsToReassign, List<RoomEntity> allRooms, boolean isOngoing) {
        RoomAssignmentLock.LOCK.lock();

        try {
            List<ResidentReassignmentPreview> previews = new ArrayList<>();

            for (RoomAssignEntity oldAssign : assignmentsToReassign) {
                Optional<RoomEntity> replacementRoom = allRooms.stream()
                        .filter(room -> !room.getId().equals(oldAssign.getRoom().getId()))
                        .filter(room -> isRoomAvailable(room, oldAssign.getFromDate(), oldAssign.getToDate()))
                        .findFirst();

                if (replacementRoom.isPresent()) {
                    RoomEntity targetRoom = replacementRoom.get();

                    // Tworzymy nowe przypisanie
                    RoomAssignEntity newAssign = new RoomAssignEntity();
                    newAssign.setResidentId(oldAssign.getResidentId());
                    newAssign.setRoom(targetRoom);
                    if (isOngoing) {
                        newAssign.setFromDate(LocalDate.now());
                    } else newAssign.setFromDate(oldAssign.getFromDate());
                    newAssign.setToDate(oldAssign.getToDate());

                    // Aktualizujemy obie strony relacji
                    //targetRoom.getRoomAssigns().add(newAssign);
                    roomAssignRepository.save(newAssign);

                    previews.add(new ResidentReassignmentPreview(
                            oldAssign.getResidentId(),
                            "N/A", // TODO tutaj pobrać z resta
                            oldAssign.getFromDate(),
                            oldAssign.getToDate(),
                            oldAssign.getFromDate(),
                            true
                    ));
                } else {
                    previews.add(new ResidentReassignmentPreview(
                            oldAssign.getResidentId(),
                            "N/A",
                            oldAssign.getFromDate(),
                            oldAssign.getToDate(),
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

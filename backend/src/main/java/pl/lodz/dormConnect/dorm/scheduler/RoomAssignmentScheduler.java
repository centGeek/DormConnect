package pl.lodz.dormConnect.dorm.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dorm.entities.DormFormEntity;
import pl.lodz.dormConnect.dorm.entities.RoomAssignEntity;
import pl.lodz.dormConnect.dorm.entities.RoomEntity;
import pl.lodz.dormConnect.dorm.repositories.DormFormRepository;
import pl.lodz.dormConnect.dorm.repositories.RoomAssignmentRepository;
import pl.lodz.dormConnect.dorm.repositories.RoomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomAssignmentScheduler {

    private final DormFormRepository dormFormRepository;
    private final RoomRepository roomRepository;
    private final RoomAssignmentRepository roomAssignRepository;

    @Transactional
    @Scheduled(fixedRate = 60000) // co minutę
    public void assignRooms() {
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

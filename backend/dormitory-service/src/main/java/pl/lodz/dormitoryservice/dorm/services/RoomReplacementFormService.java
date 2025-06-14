package pl.lodz.dormitoryservice.dorm.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.dormitoryservice.entity.RoomAssignEntity;
import pl.lodz.dormitoryservice.entity.RoomEntity;
import pl.lodz.dormitoryservice.entity.RoomReplacementFormEntity;
import pl.lodz.dormitoryservice.repository.RoomAssignmentRepository;
import pl.lodz.dormitoryservice.repository.RoomReplacementFormRepository;
import pl.lodz.dormitoryservice.repository.RoomRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomReplacementFormService {

    private final RoomRepository roomRepository;
    private final RoomReplacementFormRepository formRepository;
    private final RoomAssignmentRepository assignmentRepository;
    private final RoomService roomService;

    @Transactional
    public void acceptRoomSwapForm(Long formId) {
        RoomReplacementFormEntity form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));

        if (form.getStatus() != RoomReplacementFormEntity.FormStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form already processed");
        }

        Long requesterId = form.getRequesterId();
        Long targetId = form.getTargetStudent();

        RoomAssignEntity requesterAssignment = assignmentRepository.findCurrentAssignment(requesterId, form.getRequesterRoom())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requester not assigned"));

        RoomAssignEntity targetAssignment = assignmentRepository.findCurrentAssignment(targetId, form.getTargetRoom())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Target not assigned"));

        // Zakładamy, że swap zaczyna się od dzisiaj
        LocalDate swapStart = LocalDate.now();

        LocalDate requesterTo = requesterAssignment.getToDate();
        LocalDate targetTo = targetAssignment.getToDate();

        // Przy symulacji ignorujemy aktualne przypisania A i B
        List<RoomAssignEntity> requesterRoomAssignments = assignmentRepository
                .findAssignmentsForRoomInPeriod(form.getTargetRoom(), swapStart, requesterTo)
                .stream()
                .filter(a -> !a.getResidentId().equals(targetId)) // pomijamy B
                .toList();

        List<RoomAssignEntity> targetRoomAssignments = assignmentRepository
                .findAssignmentsForRoomInPeriod(form.getRequesterRoom(), swapStart, targetTo)
                .stream()
                .filter(a -> !a.getResidentId().equals(requesterId)) // pomijamy A
                .toList();

        boolean canMoveA = roomService.simulateAssignWithoutOverflow(requesterRoomAssignments, form.getTargetRoom(), swapStart, requesterTo);
        boolean canMoveB = roomService.simulateAssignWithoutOverflow(targetRoomAssignments, form.getRequesterRoom(), swapStart, targetTo);

        if (!canMoveA || !canMoveB) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room would be over capacity after swap");
        }

        // Zakończ stare przypisania
        requesterAssignment.setToDate(swapStart.minusDays(1));
        targetAssignment.setToDate(swapStart.minusDays(1));
        assignmentRepository.saveAll(List.of(requesterAssignment, targetAssignment));

        // Dodaj nowe przypisania
        RoomEntity requesterNewRoom = roomRepository.findById(form.getTargetRoom()).orElseThrow();
        RoomEntity targetNewRoom = roomRepository.findById(form.getRequesterRoom()).orElseThrow();

        RoomAssignEntity newA = new RoomAssignEntity();
        newA.setResidentId(requesterId);
        newA.setRoom(requesterNewRoom);
        newA.setFromDate(swapStart);
        newA.setToDate(requesterTo);

        RoomAssignEntity newB = new RoomAssignEntity();
        newB.setResidentId(targetId);
        newB.setRoom(targetNewRoom);
        newB.setFromDate(swapStart);
        newB.setToDate(targetTo);

        assignmentRepository.saveAll(List.of(newA, newB));

        form.setStatus(RoomReplacementFormEntity.FormStatus.ACCEPTED);
        form.setRespondedAt(LocalDateTime.now());
        formRepository.save(form);
    }

    public void rejectRoomSwapForm(Long formId) {
        RoomReplacementFormEntity form = formRepository.findById(formId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found"));
        if (form.getStatus() != RoomReplacementFormEntity.FormStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form already processed");
        }
        form.setStatus(RoomReplacementFormEntity.FormStatus.REJECTED);
        form.setRespondedAt(java.time.LocalDateTime.now());
        formRepository.save(form);
    }
}
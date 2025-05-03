package pl.lodz.dormConnect.dorm.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dorm.entities.DormFormEntity;
import pl.lodz.dormConnect.dorm.repositories.DormFormRepository;
import pl.lodz.dormConnect.dorm.repositories.RoomAssignmentRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DormFormService {
    private final DormFormRepository dormFormRepository;
    private final RoomAssignmentRepository roomAssignmentRepository;

    public DormFormEntity submitDormForm(DormFormEntity dormForm) {
        LocalDate endDate = handleNullEndDate(dormForm.getEndDate());
        dormForm.setEndDate(endDate);

        if (roomAssignmentRepository.existsAssignmentForStudentDuring(
                dormForm.getUserId(),
                dormForm.getStartDate(),
                endDate)) {
            throw new IllegalStateException("User already has a room assigned for this period");
        }

        return dormFormRepository.save(dormForm);
    }

    private LocalDate handleNullEndDate(LocalDate endDate) {
        return endDate == null ? LocalDate.of(2999, 1, 1) : endDate;
    }
}

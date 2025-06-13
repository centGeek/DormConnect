package pl.lodz.dormConnect.dorm.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dorm.DTO.DormFormDTO;
import pl.lodz.commons.entity.DormFormEntity;
import pl.lodz.commons.repository.jpa.DormFormRepository;
import pl.lodz.commons.repository.jpa.RoomAssignmentRepository;

import java.time.LocalDate;
import java.util.List;

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

    public void deleteForm(Long formId, Long userId) {
        DormFormEntity form = dormFormRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));

        if (form.isProcessed()) {
            throw new IllegalStateException("Cannot delete processed form");
        }

        if (!form.getUserId().equals(userId)) {
            throw new IllegalStateException("User is not authorized to delete this form");
        }

        dormFormRepository.delete(form);
    }


    public List<DormFormDTO> getUserForms(Long userId) {
        return dormFormRepository.findByUserId(userId).stream()
                .map(form -> new DormFormDTO(
                        form.getId(),
                        form.getUserId(),
                        form.getStartDate(),
                        form.getEndDate(),
                        form.isProcessed(),
                        form.getComments(),
                        form.getPriorityScore()))
                .toList();
    }
}

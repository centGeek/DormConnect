package pl.lodz.dormConnect.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.schedule.entity.CommonRoomAssigment;
import pl.lodz.dormConnect.schedule.repositories.CommonRoomAssigmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommonRoomAssigmentService {

    private final CommonRoomAssigmentRepository repository;

    @Autowired
    public CommonRoomAssigmentService(CommonRoomAssigmentRepository repository) {
        this.repository = repository;
    }

    // Pobierz wszystkie przypisania
    public List<CommonRoomAssigment> getAllAssignments() {
        return repository.findAll();
    }

    // Pobierz przypisanie na podstawie ID
    public Optional<CommonRoomAssigment> getAssignmentById(Long id) {
        return repository.findById(id);
    }

    // Utwórz nowe przypisanie z walidacją
    public CommonRoomAssigment createAssignment(CommonRoomAssigment assignment) {
        if (assignment.getStartDate().after(assignment.getEndDate())) {
            throw new IllegalArgumentException("Data rozpoczęcia nie może być późniejsza niż data zakończenia.");
        }
        return repository.save(assignment);
    }

    // Zaktualizuj istniejące przypisanie
    public Optional<CommonRoomAssigment> updateAssignment(Long id, CommonRoomAssigment updatedAssignment) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        updatedAssignment.setId(id);
        return Optional.of(repository.save(updatedAssignment));
    }

    // Usuń przypisanie na podstawie ID
    public boolean deleteAssignment(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
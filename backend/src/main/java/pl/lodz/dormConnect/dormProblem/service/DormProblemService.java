package pl.lodz.dormConnect.dormProblem.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dormProblem.dto.DormProblemDTO;
import pl.lodz.dormConnect.dormProblem.exception.DormProblemNotFoundException;
import pl.lodz.dormConnect.dormProblem.exception.IllegalProblemStatusChangeException;
import pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;
import pl.lodz.dormConnect.dormProblem.repository.DormProblemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DormProblemService {
    private final DormProblemRepository dormProblemRepository;

    @Autowired
    public DormProblemService(DormProblemRepository dormProblemRepository) {
        this.dormProblemRepository = dormProblemRepository;
    }

    @Transactional
    public DormProblemDTO createDormProblem(@NotNull DormProblemDTO dormProblemDTO) {;
        DormProblem dormProblem = DormProblemMapper.mapToEntity(dormProblemDTO);
        dormProblem.setId(0L);
        DormProblem created = dormProblemRepository.save(dormProblem);
        return DormProblemMapper.mapToDTO(created);
    }

    @Transactional
    public DormProblemDTO updateDormProblem(@NotNull DormProblemDTO dormProblemDTO) {
        DormProblem currentDormProblem = dormProblemRepository.findById(dormProblemDTO.id()).orElse(null);
        if (currentDormProblem == null) {
            throw new DormProblemNotFoundException("DormProblem with id " + dormProblemDTO.id() + " not found - cannot update");
        }
        ProblemStatus currProblemStatus = currentDormProblem.getProblemStatus();
        if(!checkProblemStatusChange(currProblemStatus, dormProblemDTO.problemStatus())) {
            throw new IllegalProblemStatusChangeException("Cannot change problem status from " + currProblemStatus + " to " + dormProblemDTO.problemStatus());
        }

        DormProblem dormProblem = DormProblemMapper.mapToEntity(dormProblemDTO);
        DormProblem saved = dormProblemRepository.save(dormProblem);
        return DormProblemMapper.mapToDTO(saved);
    }

    @Transactional
    public void deleteDormProblem(@NotNull Long id) {
        DormProblem dormProblem = dormProblemRepository.findById(id).orElse(null);
        if (dormProblem == null) {
            throw new DormProblemNotFoundException("DormProblem with id " + id + " not found");
        }
        dormProblemRepository.delete(dormProblem);
    }

    @Transactional
    public List<DormProblemDTO> getAllDormProblems() {
        return dormProblemRepository.findAll().stream()
                .map(DormProblemMapper::mapToDTO)
                .toList();
    }

    @Transactional
    public DormProblemDTO getDormProblemById(@NotNull Long id) {
        Optional<DormProblem> dormProblem = dormProblemRepository.findById(id);
        if (dormProblem.isEmpty()) {
            throw new DormProblemNotFoundException("DormProblem with id " + id + " not found");
        }
        return DormProblemMapper.mapToDTO(dormProblem.get());
    }

    private boolean checkProblemStatusChange(ProblemStatus current, ProblemStatus newStatus) {
        return (current == ProblemStatus.RESOLVED && newStatus == ProblemStatus.SUBMITTED)
                || (current == ProblemStatus.RESOLVED && newStatus == ProblemStatus.REJECTED)
                || (current == ProblemStatus.RESOLVED && newStatus == ProblemStatus.IN_PROGRESS)
                || (current == ProblemStatus.IN_PROGRESS && newStatus == ProblemStatus.SUBMITTED)
                || (current == ProblemStatus.REJECTED && newStatus == ProblemStatus.RESOLVED)
                || (current == ProblemStatus.REJECTED && newStatus == ProblemStatus.IN_PROGRESS)
                || (current == ProblemStatus.REJECTED && newStatus == ProblemStatus.SUBMITTED);
    }
}

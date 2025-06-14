package pl.lodz.dormproblemservice.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.dormproblemservice.ProblemStatus;
import pl.lodz.dormproblemservice.dto.CreateDormProblemDTO;
import pl.lodz.dormproblemservice.dto.GetDormProblemDTO;
import pl.lodz.dormproblemservice.dto.UpdateDormProblemDTO;
import pl.lodz.dormproblemservice.entity.DormProblemEntity;
import pl.lodz.dormproblemservice.mapper.DormProblemMapper;
import pl.lodz.dormproblemservice.repository.DormProblemRepository;
import pl.lodz.dormproblemservice.exception.DormProblemNotFoundException;
import pl.lodz.dormproblemservice.exception.IllegalProblemStatusChangeException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DormProblemService {

    private final DormProblemRepository dormProblemRepository;
    private final JwtService jwtService;

    public GetDormProblemDTO createDormProblem(@NotNull CreateDormProblemDTO dto, String jwt) {
        Long userId = jwtService.getIdFromToken(jwt);

        DormProblemEntity entity = DormProblemMapper.mapCreateDTOToEntity(dto);
        entity.setId(0L);
        entity.setStudentId(userId);
        entity.setProblemStatus(ProblemStatus.SUBMITTED);
        entity.setSubmittedDate(LocalDate.now());

        DormProblemEntity saved = dormProblemRepository.save(entity);
        return DormProblemMapper.mapToGetDTO(saved);
    }

    @Transactional
    public GetDormProblemDTO updateDormProblem(@NotNull UpdateDormProblemDTO dto) {
        DormProblemEntity entity = dormProblemRepository.findById(dto.id())
                .orElseThrow(() -> new DormProblemNotFoundException("DormProblem with id " + dto.id() + " not found"));

        if (checkProblemStatusChange(entity.getProblemStatus(), dto.problemStatus())) {
            throw new IllegalProblemStatusChangeException("Cannot change status from " + entity.getProblemStatus() + " to " + dto.problemStatus());
        }

        entity.setAnswer(dto.answer());
        entity.setDescription(dto.description());
        entity.setProblemStatus(dto.problemStatus());

        DormProblemEntity saved = dormProblemRepository.save(entity);
        return DormProblemMapper.mapToGetDTO(saved);
    }

    @Transactional
    public void deleteDormProblem(@NotNull Long id) {
        DormProblemEntity entity = dormProblemRepository.findById(id)
                .orElseThrow(() -> new DormProblemNotFoundException("DormProblem with id " + id + " not found"));
        dormProblemRepository.delete(entity);
    }

    @Transactional
    public List<GetDormProblemDTO> getAllDormProblems(String jwt) {
        Long userId = jwtService.getIdFromToken(jwt);
        List<String> roles = jwtService.getRolesFromToken(jwt);

        if (roles.contains("ADMIN")) {
            return dormProblemRepository.findAll().stream()
                    .map(DormProblemMapper::mapToGetDTO)
                    .toList();
        } else {
            return dormProblemRepository.findByUserId(userId).stream()
                    .map(DormProblemMapper::mapToGetDTO)
                    .toList();
        }
    }

    public GetDormProblemDTO getDormProblemById(@NotNull Long id) {
        DormProblemEntity entity = dormProblemRepository.findById(id)
                .orElseThrow(() -> new DormProblemNotFoundException("DormProblem with id " + id + " not found"));
        return DormProblemMapper.mapToGetDTO(entity);
    }

    @Transactional
    public List<GetDormProblemDTO> getDormProblemByStatus(@NotNull ProblemStatus status) {
        return dormProblemRepository.findByProblemStatus(status).stream()
                .map(DormProblemMapper::mapToGetDTO)
                .toList();
    }

    public void deleteAll() {
        dormProblemRepository.deleteAll();
    }

    public List<ProblemStatus> getAllProblemStatuses() {
        return List.of(ProblemStatus.values());
    }

    private boolean checkProblemStatusChange(ProblemStatus current, ProblemStatus newStatus) {
        return switch (current) {
            case RESOLVED -> List.of(ProblemStatus.SUBMITTED, ProblemStatus.REJECTED, ProblemStatus.IN_PROGRESS).contains(newStatus);
            case IN_PROGRESS -> newStatus == ProblemStatus.SUBMITTED;
            case REJECTED -> List.of(ProblemStatus.RESOLVED, ProblemStatus.IN_PROGRESS, ProblemStatus.SUBMITTED).contains(newStatus);
            default -> false;
        };
    }
}

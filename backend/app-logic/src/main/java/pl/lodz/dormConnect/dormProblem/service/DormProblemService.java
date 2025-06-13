package pl.lodz.dormConnect.dormProblem.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.commons.model.ProblemStatus;
import pl.lodz.dormConnect.dormProblem.dto.CreateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.GetDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.UpdateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.exception.DormProblemNotFoundException;
import pl.lodz.dormConnect.dormProblem.exception.IllegalProblemStatusChangeException;
import pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper;
import pl.lodz.commons.entity.DormProblemEntity;
import pl.lodz.commons.entity.RoleEntity;
import pl.lodz.commons.entity.UserEntity;
import pl.lodz.commons.repository.jpa.DormProblemRepository;
import pl.lodz.commons.repository.jpa.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DormProblemService {
    private final DormProblemRepository dormProblemRepository;
    private final UserRepository userRepository;

    @Autowired
    public DormProblemService(DormProblemRepository dormProblemRepository, UserRepository userRepository) {
        this.dormProblemRepository = dormProblemRepository;
        this.userRepository = userRepository;
    }

    public GetDormProblemDTO createDormProblem(@NotNull CreateDormProblemDTO createDormProblemDTO) {;
        DormProblemEntity dormProblemEntity = DormProblemMapper.mapCreateDTOToEntity(createDormProblemDTO);
        dormProblemEntity.setId(0L);
        dormProblemEntity.setProblemStatus(ProblemStatus.SUBMITTED);
        dormProblemEntity.setSubmittedDate(LocalDate.now());
        DormProblemEntity created = dormProblemRepository.save(dormProblemEntity);
        return DormProblemMapper.mapToGetDTO(created);
    }

    @Transactional
    public GetDormProblemDTO updateDormProblem(@NotNull UpdateDormProblemDTO updateDormProblemDTO) {
        DormProblemEntity currentDormProblemEntity = dormProblemRepository.findById(updateDormProblemDTO.id()).orElse(null);
        if (currentDormProblemEntity == null) {
            throw new DormProblemNotFoundException("DormProblem with id " + updateDormProblemDTO.id() + " not found - cannot update");
        }
        ProblemStatus currProblemStatus = currentDormProblemEntity.getProblemStatus();
        if(checkProblemStatusChange(currProblemStatus, updateDormProblemDTO.problemStatus())) {
            throw new IllegalProblemStatusChangeException("Cannot change problem problemStatus from " + currProblemStatus + " to " + updateDormProblemDTO.problemStatus());
        }

        currentDormProblemEntity.setAnswer(updateDormProblemDTO.answer());
        currentDormProblemEntity.setProblemStatus(updateDormProblemDTO.problemStatus());
        currentDormProblemEntity.setAnswer(updateDormProblemDTO.answer());
        currentDormProblemEntity.setDescription(updateDormProblemDTO.description());

        DormProblemEntity saved = dormProblemRepository.save(currentDormProblemEntity);
        return DormProblemMapper.mapToGetDTO(saved);
    }

    @Transactional
    public void deleteDormProblem(@NotNull Long id) {
        DormProblemEntity dormProblemEntity = dormProblemRepository.findById(id).orElse(null);
        if (dormProblemEntity == null) {
            throw new DormProblemNotFoundException("DormProblem with id " + id + " not found");
        }
        dormProblemRepository.delete(dormProblemEntity);
    }

    @Transactional
    public List<GetDormProblemDTO> getAllDormProblems() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserEntity currentUser = userRepository.findByEmail(username).orElse(null);
        if (currentUser == null) {
            throw new DormProblemNotFoundException("User with email " + username + " not found");
        }
        RoleEntity role = currentUser.getRole();
        if (role.getRoleName().equals("ADMIN")) {
            return dormProblemRepository.findAll().stream()
                    .map(DormProblemMapper::mapToGetDTO)
                    .toList();
        } else {
            List<DormProblemEntity> foundProblems =  dormProblemRepository.findByUserId(currentUser.getId());
            return foundProblems.stream()
                    .map(DormProblemMapper::mapToGetDTO)
                    .toList();
        }


    }

    public GetDormProblemDTO getDormProblemById(@NotNull Long id) {
        Optional<DormProblemEntity> dormProblem = dormProblemRepository.findById(id);
        if (dormProblem.isEmpty()) {
            throw new DormProblemNotFoundException("DormProblem with id " + id + " not found");
        }
        System.out.println("DormProblemService.getDormProblemById: " + dormProblem.get());
        return DormProblemMapper.mapToGetDTO(dormProblem.get());
    }

    @Transactional
    public List<GetDormProblemDTO> getDormProblemByStatus(@NotNull ProblemStatus problemStatus) {
        return dormProblemRepository.findByProblemStatus(problemStatus).stream()
                .map( (d) -> DormProblemMapper.mapToGetDTO((DormProblemEntity) d) )
                .toList();
    }

    public void deleteAll() {
        dormProblemRepository.deleteAll();
    }

    public List<ProblemStatus> getAllProblemStatuses() {
        List<ProblemStatus> problemStatuses =  List.of(ProblemStatus.values());
        System.out.println(problemStatuses);
        return  problemStatuses;
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

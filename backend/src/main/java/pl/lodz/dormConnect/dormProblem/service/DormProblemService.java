package pl.lodz.dormConnect.dormProblem.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.database.entity.RoleEntity;
import pl.lodz.dormConnect.database.entity.UserEntity;
import pl.lodz.dormConnect.database.repository.jpa.UserRepository;
import pl.lodz.dormConnect.dormProblem.dto.CreateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.GetDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.dto.UpdateDormProblemDTO;
import pl.lodz.dormConnect.dormProblem.exception.DormProblemNotFoundException;
import pl.lodz.dormConnect.dormProblem.exception.IllegalProblemStatusChangeException;
import pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.model.ProblemStatus;
import pl.lodz.dormConnect.dormProblem.repository.DormProblemRepository;

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
        DormProblem dormProblem = DormProblemMapper.mapCreateDTOToEntity(createDormProblemDTO);
        dormProblem.setId(0L);
        dormProblem.setProblemStatus(ProblemStatus.SUBMITTED);
        dormProblem.setSubmittedDate(LocalDate.now());
        DormProblem created = dormProblemRepository.save(dormProblem);
        return DormProblemMapper.mapToGetDTO(created);
    }

    @Transactional
    public GetDormProblemDTO updateDormProblem(@NotNull UpdateDormProblemDTO updateDormProblemDTO) {
        DormProblem currentDormProblem = dormProblemRepository.findById(updateDormProblemDTO.id()).orElse(null);
        if (currentDormProblem == null) {
            throw new DormProblemNotFoundException("DormProblem with id " + updateDormProblemDTO.id() + " not found - cannot update");
        }
        ProblemStatus currProblemStatus = currentDormProblem.getProblemStatus();
        if(checkProblemStatusChange(currProblemStatus, updateDormProblemDTO.problemStatus())) {
            throw new IllegalProblemStatusChangeException("Cannot change problem problemStatus from " + currProblemStatus + " to " + updateDormProblemDTO.problemStatus());
        }

        currentDormProblem.setAnswer(updateDormProblemDTO.answer());
        currentDormProblem.setProblemStatus(updateDormProblemDTO.problemStatus());
        currentDormProblem.setAnswer(updateDormProblemDTO.answer());
        currentDormProblem.setDescription(updateDormProblemDTO.description());

        DormProblem saved = dormProblemRepository.save(currentDormProblem);
        return DormProblemMapper.mapToGetDTO(saved);
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
            List<DormProblem> foundProblems =  dormProblemRepository.findByUserId(currentUser.getId());
            return foundProblems.stream()
                    .map(DormProblemMapper::mapToGetDTO)
                    .toList();


        }


    }

    public GetDormProblemDTO getDormProblemById(@NotNull Long id) {
        Optional<DormProblem> dormProblem = dormProblemRepository.findById(id);
        if (dormProblem.isEmpty()) {
            throw new DormProblemNotFoundException("DormProblem with id " + id + " not found");
        }
        return DormProblemMapper.mapToGetDTO(dormProblem.get());
    }

    @Transactional
    public List<GetDormProblemDTO> getDormProblemByStatus(@NotNull ProblemStatus problemStatus) {
        return dormProblemRepository.findByProblemStatus(problemStatus).stream()
                .map( (d) -> DormProblemMapper.mapToGetDTO((DormProblem) d) )
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

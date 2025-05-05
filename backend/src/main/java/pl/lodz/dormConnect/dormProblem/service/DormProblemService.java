package pl.lodz.dormConnect.dormProblem.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.dormConnect.dormProblem.dto.DormProblemDTO;
import pl.lodz.dormConnect.dormProblem.mapper.DormProblemMapper;
import pl.lodz.dormConnect.dormProblem.model.DormProblem;
import pl.lodz.dormConnect.dormProblem.repository.DormProblemRepository;

import java.util.List;

@Service
public class DormProblemService {
    private final DormProblemRepository dormProblemRepository;

    @Autowired
    public DormProblemService(DormProblemRepository dormProblemRepository) {
        this.dormProblemRepository = dormProblemRepository;
    }

    @Transactional
    public DormProblemDTO createDormProblem(DormProblemDTO dormProblemDTO) {;
        DormProblem dormProblem = DormProblemMapper.mapToEntity(dormProblemDTO);
        dormProblem.setId(0L);
        DormProblem created = dormProblemRepository.save(dormProblem);
        return DormProblemMapper.mapToDTO(created);
    }

    @Transactional
    public DormProblemDTO updateDormProblem(DormProblemDTO dormProblemDTO) {
        DormProblem dormProblem = DormProblemMapper.mapToEntity(dormProblemDTO);
        DormProblem saved = dormProblemRepository.save(dormProblem);
        return DormProblemMapper.mapToDTO(saved);
    }

    @Transactional
    public void deleteDormProblem(Long id) {
        DormProblem dormProblem = dormProblemRepository.findById(id).orElse(null);
        dormProblemRepository.delete(dormProblem);
    }

    @Transactional
    public List<DormProblemDTO> getAllDormProblems() {
        return dormProblemRepository.findAll().stream()
                .map(DormProblemMapper::mapToDTO)
                .toList();
    }

    @Transactional
    public DormProblemDTO getDormProblemById(Long id) {
        return dormProblemRepository.findById(id).map(DormProblemMapper::mapToDTO).orElse(null);
    }
}

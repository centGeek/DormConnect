package pl.lodz.dormConnect.security.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.commons.repository.ManagerRepository;
import pl.lodz.commons.model.Manager;

import java.util.List;

@AllArgsConstructor
@Service
public class ManagerRegisterService {

    private final ManagerRepository managerRepository;
    public void registerStudent(Manager manager){
        managerRepository.register(manager);
    }
    public List<Manager> getAllStudents(){
        return managerRepository.getAll();
    }
}

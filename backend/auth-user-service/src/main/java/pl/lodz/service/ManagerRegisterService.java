package pl.lodz.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.model.Manager;
import pl.lodz.repository.ManagerRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class ManagerRegisterService {

    private final ManagerRepository managerRepository;
    public void registerManager(Manager manager){
        managerRepository.register(manager);
    }
    public List<Manager> getAllStudents(){
        return managerRepository.getAll();
    }
}

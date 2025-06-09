package pl.lodz.dormConnect.users.services;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.lodz.commons.entity.UserEntity;
import pl.lodz.commons.repository.jpa.UserRepository;
import pl.lodz.dormConnect.users.dto.GetUserDTO;
import pl.lodz.dormConnect.users.mappers.UserMapper;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<GetUserDTO> getAllUsers() {
        try {
            return userRepository.findAll().stream()
                .map(UserMapper::mapToGetUserDTO)
                .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
    }
    
}

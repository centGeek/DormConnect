package pl.lodz.model;


import pl.lodz.entity.UserEntity;

public record Student(Long id, String name, String surname, UserEntity user) {
}

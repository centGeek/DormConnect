package pl.lodz.commons.model;

import pl.lodz.commons.entity.UserEntity;

public record Student(Long id, String name, String surname, UserEntity user) {
}

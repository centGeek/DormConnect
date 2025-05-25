package pl.lodz.dormConnect.domain;

import pl.lodz.entity.UserEntity;

public record Student(Long id, String name, String surname, UserEntity user) {
}

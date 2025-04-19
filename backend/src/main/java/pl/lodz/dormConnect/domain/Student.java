package pl.lodz.dormConnect.domain;

import pl.lodz.dormConnect.database.entity.UserEntity;

public record Student(Long id, String name, String surname, UserEntity user) {
}

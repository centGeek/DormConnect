package pl.lodz.dormConnect.domain;

import pl.lodz.UserEntity;

public record Student(Long id, String name, String surname, UserEntity user) {
}

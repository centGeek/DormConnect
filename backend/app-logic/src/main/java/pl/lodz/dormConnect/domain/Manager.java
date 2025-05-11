package pl.lodz.dormConnect.domain;

import pl.lodz.UserEntity;

public record Manager(Long id, String name, String surname, UserEntity user) {
}

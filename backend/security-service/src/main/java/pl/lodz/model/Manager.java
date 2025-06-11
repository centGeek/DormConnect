package pl.lodz.commons.model;

import pl.lodz.commons.entity.UserEntity;

public record Manager(Long id, String name, String surname, UserEntity user) {
}

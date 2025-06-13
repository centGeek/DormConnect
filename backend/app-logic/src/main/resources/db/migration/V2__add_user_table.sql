create table user_profile
(
    user_id   serial primary key,
    uuid      VARCHAR(255),
    user_name      varchar(64)  not null,
    card_uuid varchar(255),
    email     varchar(255) not null,
    password  varchar(255) not null,
    is_active boolean      not null,
    role_id   int      not null,
    CONSTRAINT fk_role
        FOREIGN KEY (role_id)
            REFERENCES role (role_id)
);
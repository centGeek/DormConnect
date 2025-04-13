create table manager(
    manager_id serial primary key,
    name varchar(64) not null,
    surname varchar(64) not null,
    user_id int not null,
    constraint fk_manager_user
        foreign key (user_id)
        references user_profile (user_id)
--     dormitory_id int not null,
--     constraint fk_dormitory
--         foreign key (dormitory_id)
--         references dormitory (dormitory_id)
);
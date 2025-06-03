create table student (
    student_id serial primary key,
    name varchar(64) not null,
    surname varchar(64) not null,
    user_id int not null,
    constraint fk_student_user
        foreign key (user_id)
        references user_profile (user_id)
--     room_id int not null,
--     constraint fk_room
--         foreign key (room_id)
--         references room (room_id)
);
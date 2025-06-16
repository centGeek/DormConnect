alter table student
    drop constraint fk_student_user;

alter table manager
    drop constraint fk_manager_user;

alter table student
add constraint fk_student_user_cascade
    foreign key (user_id)
    references user_profile (user_id)
    on delete cascade;

alter table manager
    add constraint fk_manager_user_cascade
        foreign key (user_id)
            references user_profile (user_id)
            on delete cascade;

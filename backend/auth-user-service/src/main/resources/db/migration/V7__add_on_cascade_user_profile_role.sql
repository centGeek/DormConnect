alter table user_profile
    drop constraint fk_role;

alter table user_profile
    add constraint fk_role
        foreign key (role_id)
            references role (role_id)
            on delete cascade;
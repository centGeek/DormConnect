insert into role (role_id, role_name)
values
    (1, 'ADMIN'),
    (2, 'MANAGER'),
    (3, 'STUDENT');

insert into user_profile (user_name, email, password, is_active, role_id)
values ('admin', 'admin@edu.p.lodz.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true, 1);


insert into user_profile (user_name, email, password, is_active, role_id)
values ('manager', 'manager@edu.p.lodz.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true, 2);


insert into user_profile (user_name, email, password, is_active, role_id)
values ('student_debil', 'student_debil@edu.p.lodz.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true, 3);

insert into role (role_id, role_name)
values
    (1, 'ADMIN'),
    (2, 'MANAGER'),
    (3, 'STUDENT');

insert into user_profile (user_name, uuid, email, password, is_active, role_id)
values ('admin', '27de1019-5fff-4f68-ad1c-854f77ded2ed', 'admin@edu.p.lodz.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true, 1);


insert into user_profile (user_name, uuid, email, password, is_active, role_id)
values ('manager', '15bde3f2-2c2e-4a61-8856-6188daf16871', 'manager@edu.p.lodz.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true, 2);


insert into user_profile (user_name, uuid, email, password, is_active, role_id)
values ('student_debil', '85ed70c6-698b-47e1-88b3-788a4c7aa5ae', 'student_debil@edu.p.lodz.pl', '$2a$12$TwQsp1IusXTDl7LwZqL0qeu49Ypr6vRdEzRq2vAsgb.zvOtrnzm5G', true, 3);

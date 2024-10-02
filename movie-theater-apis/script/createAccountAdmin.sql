use movie_theater
GO

insert into users(username, email, [password], is_enabled) values 
('admin', 'admin@gmail.com', '$2a$12$CsagpsgBM40Ni2./6s8qL.OnWNzEh/wgKOSx17OQH2/vir2hQDRM6', 1)
GO

insert into roles(role_name) values 
('ROLE_USER'),
('ROLE_STAFF'),
('ROLE_ADMIN')
GO

insert into user_roles(user_id, role_id) values (1, 3)
GO

select * from users
select * from roles
select * from user_roles




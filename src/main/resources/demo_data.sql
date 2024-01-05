insert into users (name, username, password)
values ('John Doe', 'johndoe@gmail.com', '$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.vOQ8nrEEOskrCNmGsxY7m');

insert into tasks (title, description, status, expiration_date, user_id)
values ('Buy cheese', null, 'TODO', '2023-01-29 12:00:00', 2),
       ('Do homework', 'Math, Physics, Literature', 'IN_PROGRESS', '2023-01-31 00:00:00', 2),
       ('Clean rooms', null, 'DONE', null, 2),
       ('Call Mike', 'Ask about meeting', 'TODO', '2023-02-01 00:00:00', 1);

insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');
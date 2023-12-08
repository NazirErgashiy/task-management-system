--liquibase formatted sql

--changeset 1702030352_admin.sql:1
INSERT INTO _users (name, email, password, role, create_date, update_date)
VALUES ('admin','admin@server.com','$2a$10$FwFKIrSij2V6.sjdQf.eheMldwiWmD4knlEKpULmp6bryIKhLtFlu', 'ADMIN', '08.12.2023','08.12.2023');

create table role
(
    created_at datetime(6)  not null,
    role_id    bigint       not null
        primary key,
    updated_at datetime(6)  null,
    name       varchar(255) null
);
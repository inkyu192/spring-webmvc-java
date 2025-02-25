create table permission
(
    created_at    datetime(6)  not null,
    permission_id bigint       not null
        primary key,
    updated_at    datetime(6)  null,
    name          varchar(255) null
);
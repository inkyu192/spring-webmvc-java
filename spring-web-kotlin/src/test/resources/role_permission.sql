create table role_permission
(
    created_at         datetime(6) not null,
    permission_id      bigint      null,
    role_id            bigint      null,
    role_permission_id bigint      not null
        primary key,
    updated_at         datetime(6) null,
    constraint FKa6jx8n8xkesmjmv6jqug6bg68
        foreign key (role_id) references role (role_id),
    constraint FKf8yllw1ecvwqy3ehyxawqa1qp
        foreign key (permission_id) references permission (permission_id)
);
create table member_role
(
    created_at     datetime(6) not null,
    member_id      bigint      null,
    member_role_id bigint      not null
        primary key,
    role_id        bigint      null,
    updated_at     datetime(6) null,
    constraint FK34g7epqlcxqloewku3aoqhhmg
        foreign key (member_id) references member (member_id),
    constraint FKdiix07v86r3ntrbs3l02qr7y0
        foreign key (role_id) references role (role_id)
);

create table member_permission
(
    created_at           datetime(6) not null,
    member_id            bigint      null,
    member_permission_id bigint      not null
        primary key,
    permission_id        bigint      null,
    updated_at           datetime(6) null,
    constraint FK9ussnlg13pft850kaolbu0fg7
        foreign key (member_id) references member (member_id),
    constraint FKg92dystem9riwv5vpad4gcjhm
        foreign key (permission_id) references permission (permission_id)
);
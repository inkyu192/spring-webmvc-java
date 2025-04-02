create table notification
(
    is_read         bit          not null,
    created_at      datetime(6)  not null,
    member_id       bigint       null,
    notification_id bigint       not null
        primary key,
    updated_at      datetime(6)  null,
    message         varchar(255) null,
    title           varchar(255) null,
    url             varchar(255) null,
    constraint FK1xep8o2ge7if6diclyyx53v4q
        foreign key (member_id) references member (member_id)
);
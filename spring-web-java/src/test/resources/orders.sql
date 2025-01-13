create table orders
(
    created_date       datetime(6)              null,
    delivery_id        bigint                   null,
    last_modified_date datetime(6)              null,
    member_id          bigint                   null,
    order_date         datetime(6)              null,
    order_id           bigint                   not null
        primary key,
    status             enum ('CANCEL', 'ORDER') null,
    constraint UK9ct0l8xfeaiqruabcqjh1neui
        unique (delivery_id),
    constraint FKpktxwhj3x9m4gth5ff6bkqgeb
        foreign key (member_id) references member (member_id),
    constraint FKtkrur7wg4d8ax0pwgo0vmy20c
        foreign key (delivery_id) references delivery (delivery_id)
);
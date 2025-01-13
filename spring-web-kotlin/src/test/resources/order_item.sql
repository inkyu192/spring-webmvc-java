create table order_item
(
    count              int         not null,
    order_price        int         not null,
    created_date       datetime(6) null,
    item_id            bigint      null,
    last_modified_date datetime(6) null,
    order_id           bigint      null,
    order_item_id      bigint      not null
        primary key,
    constraint FKija6hjjiit8dprnmvtvgdp6ru
        foreign key (item_id) references item (item_id),
    constraint FKt4dc2r9nbvbujrljv3e23iibt
        foreign key (order_id) references orders (order_id)
);
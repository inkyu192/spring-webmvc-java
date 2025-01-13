create table item
(
    price              int                               not null,
    quantity           int                               not null,
    created_date       datetime(6)                       null,
    item_id            bigint                            not null
        primary key,
    last_modified_date datetime(6)                       null,
    description        varchar(255)                      null,
    name               varchar(255)                      null,
    category           enum ('ROLE_BOOK', 'ROLE_TICKET') null
);
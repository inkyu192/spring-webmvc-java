create table delivery
(
    delivery_id bigint                           not null
        primary key,
    city        varchar(255)                     null,
    street      varchar(255)                     null,
    zipcode     varchar(255)                     null,
    status      enum ('CANCEL', 'COMP', 'READY') null
);
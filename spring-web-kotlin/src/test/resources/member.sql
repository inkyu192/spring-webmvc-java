create table member
(
    created_date       datetime(6)                                      null,
    last_modified_date datetime(6)                                      null,
    member_id          bigint                                           not null
        primary key,
    account            varchar(255)                                     null,
    city               varchar(255)                                     null,
    name               varchar(255)                                     null,
    password           varchar(255)                                     null,
    street             varchar(255)                                     null,
    zipcode            varchar(255)                                     null,
    role               enum ('ROLE_ADMIN', 'ROLE_BUYER', 'ROLE_SELLER') null
);
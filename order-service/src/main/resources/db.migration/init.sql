create database  order_service;

use order_service;

create table orders
(
    id                 varchar(36)          not null
        primary key,
    customer_id        varchar(255)         not null,
    status             varchar(255)         not null,
    total_amount       decimal(10,2)                  not null,
    is_deleted         tinyint(1) default 0 null,
    created_at         timestamp(6)         null,
    created_by         varchar(255)         null,
    updated_at         timestamp(6)         null,
    updated_by         varchar(255)         null
);

create table order_items
(
    id                 varchar(36)          not null
        primary key,
    order_id           varchar(36)          not null,
    product_id         varchar(255)         not null,
    price              decimal(10,2)                  not null,
    quantity           int                  not null,
    is_deleted         tinyint(1) default 0 null,
    created_at         timestamp(6)         null,
    created_by         varchar(255)         null,
    updated_at         timestamp(6)         null,
    updated_by         varchar(255)         null,
    constraint fk_order_items_order
        foreign key (order_id) references orders (id)
);

alter table orders add column promotion_applied tinyint(1) default 0;

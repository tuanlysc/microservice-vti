create database product_service;

use product_service;

create table categories(
                           id varchar(36) primary key ,
                           name varchar(255) not null ,
                           parent_id varchar(36),
                           is_deleted boolean,
                           created_at timestamp,
                           created_by varchar(255),
                           updated_at timestamp,
                           updated_by varchar(255)
);

create table products(
                         id varchar(36) primary key,
                         name varchar(255) not null,
                         price decimal(8,2),
                         stock int,
                         category_id varchar(36),
                         is_deleted boolean,
                         created_at timestamp,
                         created_by varchar(255),
                         updated_at timestamp,
                         updated_by varchar(255)
);
alter table categories
    add constraint fk_category_parent
        foreign key (parent_id)
            references categories(id);

alter table products
    add constraint fk_product_category
        foreign key (category_id)
            references categories(id);
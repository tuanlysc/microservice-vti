create database promotion_service;

use product_service;

create table promotions(
    id varchar(36) primary key,
    code varchar(255) not null,
    name varchar(255) not null,
    discount_type enum('PERCENTAGE','FIXED_AMOUNT'),
    discount_value ,
    max_discount_amount ,
    min_order_value ,
    usage_limit ,
    used_count ,
    stated_at timestamp,
    end_at timestamp,
    status enum('ACTIVE','INACTIVE') default 'ACTIVE',
    created_at timestamp,
    created_by varchar(255),
    updated_at timestamp,
    updated_by varchar(255)
);

create table promotion_usages(

)
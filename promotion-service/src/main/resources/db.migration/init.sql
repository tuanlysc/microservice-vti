create database promotion_service;

use product_service;

create table promotions(
    id varchar(36) primary key,
    code varchar(255) not null unique ,
    name varchar(255) not null,
    discount_type enum('PERCENTAGE','FIXED_AMOUNT'),
    discount_value DECIMAL(10,2) NOT NULL,
    max_discount_amount DECIMAL(10,2) NULL,
    min_order_value DECIMAL(10,2) DEFAULT 0.00 NOT NULL,
    usage_limit int DEFAULT NULL,
    used_count int default 0 not null,
    started_at timestamp,
    ended_at timestamp,
    status enum('ACTIVE','INACTIVE') default 'ACTIVE',
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255)
);

create table promotion_usages(
    id varchar(36) primary key,
    promotion_id varchar(36),
    user_id varchar(36),
    order_id varchar(36),
    discount_applied decimal(8,2),
    order_amount decimal(8,2),
    used_at timestamp,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id)
)
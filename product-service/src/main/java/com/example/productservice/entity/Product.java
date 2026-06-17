package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    @Column(name = "price", precision = 8, scale = 2)
    BigDecimal price;
    Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}

package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByCategoryId(String categoryId);

    @Query("select p from Product  p where lower(p.name) like lower(concat('%', :name ,'%') ) and p.isDeleted=false ")
    List<Product> findByProductName(@Param("name") String productName);

    List<Product> findByIdIn(List<String> productIds);
}

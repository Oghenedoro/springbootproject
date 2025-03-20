package com.example.demo.repositories;


import com.example.demo.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order,Long> {

 /* *//*  @Query("SELECT o FROM Order o JOIN o.customer c WHERE " +
            "LOWER(a.content) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.author) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.category) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.keywords) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Article> findArticlesBySearchText(@Param("searchText") String*//*
  @Query("SELECT o FROM Order o JOIN o.customer c WHERE LOWER(c.name LIKE %:customerName%"+
     )*/
    List<Order> findByCustomerName(@Param("customerName") String customerName);

    List<Order> findByCustomerId(Long customerId);
}

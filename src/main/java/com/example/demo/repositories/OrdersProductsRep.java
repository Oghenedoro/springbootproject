package com.example.demo.repositories;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrdersProducts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersProductsRep extends JpaRepository<OrdersProducts, Long> {
   // List<OrdersProducts> findByOrderId(Long orderId);

    void deleteAllByOrder(Order order);

    // List<OrdersProducts> findByOrderId(Long orderId);
}

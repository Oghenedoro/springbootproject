package com.example.demo.dtos;

import com.example.demo.entities.OrdersProducts;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {

    private Long orderId;
    private Long customerId;
    private List<OrdersProductsDto> ordersProducts;
    private LocalDate orderDate;


    public OrderDto() {

    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrdersProductsDto> getOrdersProducts() {
        return ordersProducts;
    }

    public void setOrdersProducts(List<OrdersProductsDto> ordersProducts) {
        this.ordersProducts = ordersProducts;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}

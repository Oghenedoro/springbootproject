package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity(name="orders")
public class Order implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long customerId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId",updatable = false,insertable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE)
    private List<OrdersProducts> ordersProducts;
    private LocalDate orderDate;

    public Customer getCustomer() {
        return customer;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrdersProducts> getOrdersProducts() {
        return ordersProducts;
    }

    public void setOrdersProducts(List<OrdersProducts> ordersProducts) {
        this.ordersProducts = ordersProducts;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
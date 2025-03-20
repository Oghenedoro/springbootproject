package com.example.demo.dtos;

import com.example.demo.entities.Order;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDto {

    private Long customerId;
    private String name;
    private List<Long> idOrders;

    public String getName() {
        return name;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getIdOrders() {
        return idOrders;
    }

    public void setIdOrders(List<Long> idOrders) {
        this.idOrders = idOrders;
    }
}

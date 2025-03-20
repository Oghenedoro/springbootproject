package com.example.demo.dtos;

import lombok.Data;

import java.util.Objects;

@Data
public class ProductDto {

    private Long productId;
    private String name;
    private Double price;

    public String getName() {
        return name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

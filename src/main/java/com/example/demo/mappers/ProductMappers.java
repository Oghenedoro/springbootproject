package com.example.demo.mappers;

import com.example.demo.dtos.ProductDto;
import com.example.demo.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMappers {

    public ProductDto mapFromProductToDto(Product product);
    public Product mapFromDtoToProductEntity(ProductDto productDto);
}

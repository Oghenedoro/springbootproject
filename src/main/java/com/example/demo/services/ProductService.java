package com.example.demo.services;

import com.example.demo.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    public ProductDto creatProduct(ProductDto productDto);
    public ProductDto updateProduct(ProductDto productDto,Long idProduct);
    public List<ProductDto> getAllProducts();
    public boolean deletProduct(Long idProduct);

}

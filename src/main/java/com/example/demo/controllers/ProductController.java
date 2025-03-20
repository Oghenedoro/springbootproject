package com.example.demo.controllers;

import com.example.demo.dtos.ProductDto;
import com.example.demo.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<ProductDto> creatProduct(@RequestBody ProductDto productDto){
        productDto = productService.creatProduct(productDto);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }
}

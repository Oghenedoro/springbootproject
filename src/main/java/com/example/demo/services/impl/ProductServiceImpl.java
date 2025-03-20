package com.example.demo.services.impl;

import com.example.demo.dtos.ProductDto;
import com.example.demo.entities.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappers.ProductMappers;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMappers productMappers;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductMappers productMappers, ProductRepository productRepository) {
        this.productMappers = productMappers;
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto creatProduct(ProductDto productDto) {

        Product product = productMappers.mapFromDtoToProductEntity(productDto);
        product = productRepository.save(product);
        return productMappers.mapFromProductToDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long idProduct) {

        Optional<Product> product = Optional.ofNullable(productRepository.findById(idProduct)
                .orElseThrow(() -> new ResourceNotFoundException("Product non trouvé !")));
        product.get().setProductId(productDto.getProductId());
        product.get().setName(productDto.getName());
        product.get().setPrice(productDto.getPrice());

        return productMappers.mapFromProductToDto(product.get());
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> productMappers.mapFromProductToDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletProduct(Long idProduct) {

        Optional<Product>product = Optional.ofNullable(productRepository.findById(idProduct)
                .orElseThrow(() -> new ResourceNotFoundException("Product non trouvé !")));
       productRepository.delete(product.get());

       return true;
    }
}

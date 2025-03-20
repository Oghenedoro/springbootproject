package com.example.demo;

import com.example.demo.dtos.ProductDto;
import com.example.demo.entities.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappers.ProductMappers;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductMappers productMappers;

    @Mock
    private ProductRepository productRepository;

    private ProductDto productDto;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initializing a sample ProductDto and Product for testing
        productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setName("Sample Product");
        productDto.setPrice(100.0);

        product = new Product();
        product.setProductId(1L);
        product.setName("Sample Product");
        product.setPrice(100.0);
    }

    @Test
    public void testCreatProduct() {
        // Arrange
        when(productMappers.mapFromDtoToProductEntity(productDto)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMappers.mapFromProductToDto(product)).thenReturn(productDto);

        // Act
        ProductDto createdProductDto = productService.creatProduct(productDto);

        // Assert
        assertNotNull(createdProductDto);
        assertEquals(productDto.getProductId(), createdProductDto.getProductId());
        assertEquals(productDto.getName(), createdProductDto.getName());
        assertEquals(productDto.getPrice(), createdProductDto.getPrice());

        // Verify that the mapper and repository methods are called
        verify(productMappers).mapFromDtoToProductEntity(productDto);
        verify(productRepository).save(product);
        verify(productMappers).mapFromProductToDto(product);
    }
    @Test
    public void testUpdateProduct_Success(){

        // Given
        productDto = new ProductDto();
        productDto.setProductId(1L);
        productDto.setName("Updated Product");
        productDto.setPrice(150.0);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMappers.mapFromProductToDto(product)).thenReturn(productDto);

        ProductDto dto = productService.updateProduct(productDto, 1L);

        // Then
        assertNotNull(dto);
        assertEquals(productDto.getProductId(), dto.getProductId());
        assertEquals("Updated Product", dto.getName());
        assertEquals(150.0, dto.getPrice());
        verify(productRepository).findById(1L);
        assertEquals(1L, product.getProductId());
        assertEquals("Updated Product", product.getName());
        assertEquals(150.0, product.getPrice());
    }

    @Test
    public void testUpdateProduct_ProductNotFound() {
        // Given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(productDto, 1L);
        });

        assertEquals("Product non trouvé !", exception.getMessage());
        verify(productRepository).findById(1L);
    }

    @Test
    public void testGetAllProducts_Success() {
        // Given
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);
        when(productMappers.mapFromProductToDto(product)).thenReturn(productDto);

        // When
        List<ProductDto> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample Product", result.get(0).getName());
        verify(productRepository).findAll();
        verify(productMappers).mapFromProductToDto(product);
    }

    @Test
    public void testDeletProduct_Success() {
        // Given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // When
        boolean result = productService.deletProduct(1L);

        // Then
        assertTrue(result);
        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }
}

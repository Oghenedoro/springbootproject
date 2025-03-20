package com.example.demo;

import com.example.demo.dtos.OrderDto;
import com.example.demo.dtos.OrderResponse;
import com.example.demo.dtos.OrdersProductsDto;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrdersProducts;
import com.example.demo.entities.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappers.OrderMapper;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.OrdersProductsRep;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrdersProductsRep ordersProductsRep;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Product product;
    private Order order;
    private OrderDto orderDto;
    private Customer customer1;
    private Customer customer2;
    private Order order1;
    private Order order2;
    private Product product1;
    private Product product2;
    private OrdersProducts ordersProducts;
    private OrdersProductsDto ordersProductsDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Arrange
        orderDto = new OrderDto();
        orderDto.setOrderId(1L);
        orderDto.setCustomerId(1L);
        orderDto.setOrdersProducts(new ArrayList<>());

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John Doe");
        customer.setOrders(Collections.singletonList(order));

        order = new Order();
        order.setOrderId(orderDto.getOrderId());
        order.setCustomerId(orderDto.getCustomerId());
        order.setCustomer(customer);

        product = new Product();
        product.setProductId(1L);
        product.setPrice(100.0);
        product.setName("product1");

        ordersProductsDto = new OrdersProductsDto();
        ordersProductsDto.setOrderProductId(1L);
        ordersProductsDto.setOrderId(1L);
        ordersProductsDto.setProductId(1L);
        orderDto.setOrdersProducts(Collections.singletonList(ordersProductsDto));

        ordersProducts = new OrdersProducts();
        ordersProducts.setProduct(product);
        ordersProducts.setPrice(25.23);
        ordersProducts.setOrder(order);
        order.setOrdersProducts(Collections.singletonList(ordersProducts));

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProductName(product.getName());
    }

    @Test
    public void testCreateOrder_Success() {

        when(orderMapper.mapFromOrderDtoToOrder(orderDto)).thenReturn(order);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.mapFronOrdersProductsDTOtoEntity(orderDto.getOrdersProducts().get(0))).thenReturn(ordersProducts);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(ordersProductsRep.save(any(OrdersProducts.class))).thenReturn(ordersProducts);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());
        when(orderMapper.mapFromOrderToOrderDto(order)).thenReturn(orderDto);

        // Act
        OrderDto result = orderService.createOrder(orderDto);

        // Assert
        assertNotNull(result);
        assertEquals(customer.getCustomerId(), result.getCustomerId()); // Verify that the customer is set
        verify(orderRepository, times(1)).save(any(Order.class)); // Ensure order is saved
        verify(ordersProductsRep, times(1)).save(ordersProducts); // Ensure order products are saved
    }

    @Test
    public void testCreateOrder_CustomerNotFound() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(orderDto);
        });
    }

    @Test
    public void testCreateOrder_ProductNotFound() {
        // Arrange
        List<OrdersProductsDto>dtos = new ArrayList<>();
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setOrdersProducts(Collections.singletonList(new OrdersProductsDto()));
        OrdersProductsDto dto = new OrdersProductsDto();
        dto.setProductId(1L);
        dtos.add(dto);
        orderDto.setOrdersProducts(dtos);

        Customer customer = new Customer();
        customer.setCustomerId(1L);

        when(orderMapper.mapFromOrderDtoToOrder(orderDto)).thenReturn(new Order());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderMapper.mapFronOrdersProductsDTOtoEntity(dto)).thenReturn(new OrdersProducts());
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(orderDto);
        });
    }

    @Test
    public void testUpdateOrder_Success() {
        // Given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(orderMapper.mapFronOrdersProductsDTOtoEntity(any())).thenReturn(ordersProducts);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(ordersProductsRep.save(any(OrdersProducts.class))).thenReturn(new OrdersProducts());
        when(orderMapper.mapFronOrdersProductsToDTO(any())).thenReturn(new OrdersProductsDto());

        // Act
        OrderDto updatedOrderDto = orderService.updateOrder(1L, orderDto);

        // Assert
        verify(orderRepository).findById(1L);
        verify(customerRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
        assert updatedOrderDto != null;
        assert updatedOrderDto.getCustomerId().equals(1L);
        assert updatedOrderDto.getOrdersProducts() != null;
        // You can also add more assertions for the contents of ordersProducts in updatedOrderDto if necessary
    }

    @Test
    public void testUpdateOrder_OrderNotFound() {
        // Given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        Exception e = assertThrows(ResourceNotFoundException.class, () ->{
            orderService.updateOrder(1L, orderDto);
        });
        assertEquals("Order non trouvé !", e.getMessage());
        verify(orderRepository).findById(1L);
    }

    @Test
    public void testUpdateOrder_CustomerNotFound() {
        // Given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        try {
            orderService.updateOrder(1L, orderDto);
        } catch (ResourceNotFoundException e) {
            assert e.getMessage().equals("Customer nontrouvé !");
        }
        verify(orderRepository).findById(1L);
        verify(customerRepository).findById(1L);
    }

    @Test
    public void testUpdateOrder_ProductNotFound() {
        // Given
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(orderMapper.mapFronOrdersProductsDTOtoEntity(any())).thenReturn(new OrdersProducts());
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        try {
            orderService.updateOrder(1L, orderDto);
        } catch (ResourceNotFoundException e) {
            assert e.getMessage().equals("Product none trouvé !");
        }
        verify(orderRepository).findById(1L);
        verify(customerRepository).findById(1L);
        verify(productRepository).findById(anyLong());
    }

    @Test
    public void testFindById_Success() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.mapFromOrderToOrderDto(order)).thenReturn(orderDto);

        OrderDto foundOrderDto = orderService.findById(1L);

        verify(orderRepository).findById(1L);
        assertNotNull(foundOrderDto);
    }

    @Test
    public void testFindById_OrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            orderService.findById(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Order non trouvé !", e.getMessage());
        }

        verify(orderRepository).findById(1L);
    }

    @Test
    public void testFindAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.mapFromOrderToOrderDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderService.findAllOrders();

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    public void testFindByCustomerName_Success() {
        String customerName = "John Doe";
        when(orderRepository.findByCustomerName(customerName)).thenReturn(List.of(order));
        when(orderMapper.mapFromOrderToOrderDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderService.findByCustomerName(customerName);

        assertEquals(1, result.size());
        verify(orderRepository).findByCustomerName(customerName);
    }

    @Test
    public void testDeletOrder_Success() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        doNothing().when(ordersProductsRep).deleteAllByOrder(any());

        boolean result = orderService.deletOrder(1L);

        verify(orderRepository).findById(1L);
        verify(ordersProductsRep).deleteAllByOrder(order);
        verify(orderRepository).delete(order);
        assertTrue(result);
    }

    @Test
    public void testDeletOrder_OrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            orderService.deletOrder(1L);
        } catch (ResourceNotFoundException e) {
            assertEquals("Order non trouvé !", e.getMessage());
        }

        verify(orderRepository).findById(1L);
    }

    @Test
    public void testGetOrdersByCustomerId() {
        Long customerId = 1L;
        when(orderRepository.findByCustomerId(customerId)).thenReturn(Collections.singletonList(order));
        when(orderMapper.mapFromOrderToOrderDto(order)).thenReturn(orderDto);

        List<OrderResponse> result = orderService.getOrdersByCustomerId(customerId);

        assertEquals(1, result.size());
        verify(orderRepository).findByCustomerId(customerId);
    }

    @Test
    public void testDisplayOrderDetails() {
        order.setOrdersProducts(List.of(ordersProducts));
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.mapFromOrderToOrderDto(order)).thenReturn(orderDto);

        List<OrderResponse> result = orderService.displayOrderDetails();

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }
}

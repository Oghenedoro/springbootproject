package com.example.demo.services;

import com.example.demo.dtos.OrderDto;
import com.example.demo.dtos.OrderResponse;
import java.util.List;

public interface OrderService {

    public OrderDto createOrder(OrderDto orderDto);
    public OrderDto findById(Long orderId);
    public List<OrderDto>findAllOrders();
    List<OrderDto> findByCustomerName(String customerName);
    public boolean deletOrder(Long orderId);
    public OrderDto updateOrder(Long orderId,OrderDto orderDto);
    List<String> searchKeywords(String keyword);
    List<OrderResponse> displayOrderDetails();
    public List<OrderResponse> getOrdersByCustomerId(Long customerId);
}

package com.example.demo.controllers;

import com.example.demo.dtos.OrderDto;
import com.example.demo.dtos.OrderResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        orderDto = orderService.createOrder(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }
    @RequestMapping(value = "/ordersbyid/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<OrderDto> findById(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.findById(orderId);
        return ResponseEntity.ok(orderDto);
    }
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDto>> findAllOrders() {
        List<OrderDto>orderDtos = orderService.findAllOrders();
        return ResponseEntity.ok(orderDtos);
    }
    @RequestMapping(value = "/orders/{customerName}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDto>> findByCustomerName(@PathVariable String customerName) {
        List<OrderDto>orderDtos = orderService.findByCustomerName(customerName);
        return ResponseEntity.ok(orderDtos);
    }
    @RequestMapping(value = "/orders/{orderId}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deletOrder(@PathVariable Long orderId) {
        orderService.deletOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("orders/details")
    public ResponseEntity<List<OrderResponse>> displayOrderDetails(){
        List<OrderResponse> orderResponses= orderService.displayOrderDetails();
        return ResponseEntity.ok(orderResponses);
    }
    @GetMapping("orders/details/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderResponse> orderResponses= orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<String>> searchKeywords(@PathVariable String keyword) {
        List<String> matchingKeywords = orderService.searchKeywords(keyword);
        return ResponseEntity.ok(matchingKeywords);
    }
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long orderId, @RequestBody OrderDto updatedOrderDto) {
        try {
            OrderDto updatedOrder = orderService.updateOrder(orderId, updatedOrderDto);
            return ResponseEntity.ok(updatedOrder);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
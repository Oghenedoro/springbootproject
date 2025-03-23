package com.example.demo.services.impl;

import com.example.demo.dtos.*;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrdersProducts;
import com.example.demo.entities.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mappers.CustomerMapper;
import com.example.demo.mappers.OrderMapper;
import com.example.demo.mappers.ProductMappers;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.OrdersProductsRep;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final CustomerMapper customerMapper;
    private final ProductMappers productMappers;
    private final OrdersProductsRep ordersProductsRep;

    public OrderServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository, OrderRepository orderRepository, OrderMapper mapper, CustomerMapper customerMapper, ProductMappers productMappers, OrdersProductsRep ordersProductsRep) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.customerMapper = customerMapper;
        this.productMappers = productMappers;
        this.ordersProductsRep = ordersProductsRep;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        Order order = mapper.mapFromOrderDtoToOrder(orderDto);

        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        order.setCustomer(customer);
        order.setOrderDate(LocalDate.now());
        orderRepository.save(order);

        List<OrdersProducts> ordersProductsList = new ArrayList<>();

        for (OrdersProductsDto dto : orderDto.getOrdersProducts()) {
            Long idProduct = dto.getProductId();
            int quantity = dto.getQuantity();
            Optional<Product> product = productRepository.findById(idProduct);

            // Vérification de l'existence des produits
            if (!product.isPresent()) {
                throw new ResourceNotFoundException("No Products found");
            }
            OrdersProducts ordersProducts = mapper.mapFronOrdersProductsDTOtoEntity(dto);

            ordersProducts.setProduct(product.get());
            ordersProducts.setOrder(order);
            ordersProducts.setQuantity(quantity);
            ordersProducts.setPrice(product.get().getPrice());

            ordersProducts = ordersProductsRep.save(ordersProducts);
            ordersProductsList.add(ordersProducts);
        }
        order.setOrdersProducts(ordersProductsList);
        return mapper.mapFromOrderToOrderDto(order);
    }

    @Override
    public OrderDto findById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Order non trouvé !"));
        return mapper.mapFromOrderToOrderDto(order);
    }

    @Override
    public List<OrderDto> findAllOrders() {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        return orders.stream()
                .map(mapper::mapFromOrderToOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> findByCustomerName(String customerName) {
        List<Order> orders = orderRepository.findByCustomerName(customerName);
        return orders.stream().map(mapper::mapFromOrderToOrderDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deletOrder(Long orderId) {
        Optional<Order> order = Optional.ofNullable(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order non trouvé !")));

        ordersProductsRep.deleteAllByOrder(order.get());

        order.get().setCustomer(null);
        orderRepository.delete(order.get());
        return true;
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Order non trouvé !"));
        order.setOrderId(order.getOrderId());
        Optional<Customer> customer = Optional.ofNullable(customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer nontrouvé !")));
        customer.get().setCustomerId(customer.get().getCustomerId());

        order.setOrderDate(LocalDate.now());
        orderRepository.save(order);

        List<OrdersProductsDto> ordersProductsDtos = new ArrayList<>();
        for(OrdersProductsDto dto: orderDto.getOrdersProducts()){

            Long idProduct = dto.getProductId();
            OrdersProducts ordersProducts = mapper.mapFronOrdersProductsDTOtoEntity(dto);
            Product product = productRepository.findById(idProduct)
                    .orElseThrow(()-> new ResourceNotFoundException("Product none trouvé !"));
            ordersProducts.setProduct(product);
            ordersProducts.setOrder(order);
            ordersProducts.setPrice(product.getPrice());
            ordersProducts.setQuantity(ordersProducts.getQuantity());
            ordersProducts = ordersProductsRep.save(ordersProducts);
            OrdersProductsDto dto1 = mapper.mapFronOrdersProductsToDTO(ordersProducts);
            ordersProductsDtos.add(dto1);
        }
        orderDto.setOrdersProducts(ordersProductsDtos);
        return orderDto;
    }
    @Override
    public List<String> searchKeywords(String keyword) {
        List<Order> allOrders = (List<Order>) orderRepository.findAll();

        Set<String> matchingKeywords = new HashSet<>();

        for (Order order : allOrders) {
            // Check if the customer's name contains the keyword

            String customerName = order.getCustomer().getName();
            if (customerName.toLowerCase().contains(keyword.toLowerCase())) {
                matchingKeywords.add(customerName);
            }
            // Check if any product's name contains the keyword
            List<String> productNames = order.getOrdersProducts().stream()
                    .filter(orderProduct -> orderProduct.getProduct().getName().toLowerCase().contains(keyword.toLowerCase()))
                    .map(op -> op.getProduct().getName())
                    .collect(Collectors.toList());

            matchingKeywords.addAll(productNames);
        }
        return new ArrayList<>(matchingKeywords);
    }
    @Override
    public List<OrderResponse> displayOrderDetails() {
        // Fetch all orders from the repository
        List<Order> orders = (List<Order>) orderRepository.findAll();
        // Map orders to responses
        return orders.stream()
                .filter(order -> order.getCustomer() != null) // Ensure the order has a customer
                .filter(order -> order.getOrdersProducts() != null)
                .flatMap(order -> order.getOrdersProducts().stream()
                        .map(ordersProduct -> mapToOrderResponse(order, ordersProduct)))
                .collect(Collectors.toList());
    }
    // Helper method to map Order and OrdersProducts to OrderResponse
    private OrderResponse mapToOrderResponse(Order order, OrdersProducts ordersProduct) {
        OrderResponse response = new OrderResponse();

        // Set properties
        response.setProductName(ordersProduct.getProduct().getName());
        response.setCustomerName(order.getCustomer().getName());
        response.setProductPrice(ordersProduct.getPrice());
        response.setQuantity(ordersProduct.getQuantity());

        return response;
    }

    @Override
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
       return orders.stream().flatMap(order -> order.getOrdersProducts().stream()
                        .map(ordersProducts -> mapToOrderResponse(order, ordersProducts)))
                .collect(Collectors.toList());

    }
}
package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.OrderDto;
import com.example.demo.dtos.OrdersProductsDto;
import com.example.demo.dtos.ProductDto;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrdersProducts;
import com.example.demo.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "customer.customerId", target = "customerId") // Mapping customer ID
   // @Mapping(target = "idOrdersProducts", expression = "java(orderProductsToIds(order.getOrdersProducts()))") // Custom mapping for products
    OrderDto mapFromOrderToOrderDto(Order order);

    @Mapping(source = "customerId", target = "customer.customerId")
    Order mapFromOrderDtoToOrder(OrderDto orderDto);


    @Mapping(source = "id", target = "orderProductId")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "order.orderId", target = "orderId")
    OrdersProductsDto mapFronOrdersProductsToDTO(OrdersProducts entity);
    @Mapping(source = "orderProductId", target = "id")
    @Mapping(source = "productId", target = "product.productId")
    @Mapping(source = "orderId", target = "order.orderId")
    OrdersProducts mapFronOrdersProductsDTOtoEntity(OrdersProductsDto dto);

    CustomerDto mapFromCustomerToCustomerDto(Customer customer);
    Customer mapFromCcustomerDtoToCustomer(CustomerDto customerDto);

    ProductDto productToProductDto(Product product);
    Product productDtoToProduct(ProductDto productDto);

    default List<Long> orderProductsToIds(List<OrdersProducts> products) {
        return products.stream().map(op -> op.getOrder().getOrderId()).toList(); // Extracting order IDs
    }
}


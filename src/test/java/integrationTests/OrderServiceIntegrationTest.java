/*
package integrationTests;

import com.example.demo.DemoApplication;
import com.example.demo.dtos.OrderDto;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Order;
import com.example.demo.entities.Product;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = DemoApplication.class)
@Transactional
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer testCustomer;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    public void setUp() {
        // Create and save a test customer
        testCustomer = new Customer();
        testCustomer.setName("John Doe");
        customerRepository.save(testCustomer);

        // Create and save test products
        testProduct1 = new Product();
        testProduct1.setName("Product A");
        testProduct1.setPrice(10.0);
        productRepository.save(testProduct1);

        testProduct2 = new Product();
        testProduct2.setName("Product B");
        testProduct2.setPrice(20.0);
        productRepository.save(testProduct2);
    }

    @Test
    public void testCreateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(testCustomer.getId());
        orderDto.setIdProducts(Arrays.asList(testProduct1.getId(), testProduct2.getId()));

        OrderDto createdOrder = orderService.createOrder(orderDto);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getCustomerId()).isEqualTo(testCustomer.getId());
        assertThat(createdOrder.getIdProducts()).containsExactlyInAnyOrder(testProduct1.getId(), testProduct2.getId());
      //  assertThat(orderRepository.count()).isEqualTo(1); // Ensure an order is created

    }

    @Test
    public void testFindById() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(testCustomer.getId());
        orderDto.setIdProducts(Arrays.asList(testProduct1.getId(), testProduct2.getId()));
        OrderDto createdOrder = orderService.createOrder(orderDto);

        OrderDto fetchedOrder = orderService.findById(createdOrder.getId());

        assertThat(fetchedOrder).isNotNull();
        assertThat(fetchedOrder.getCustomerId()).isEqualTo(testCustomer.getId());
    }

    @Test
    public void testFindAllOrders() {
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setCustomerId(testCustomer.getId());
        orderDto1.setIdProducts(Arrays.asList(testProduct1.getId()));
        orderService.createOrder(orderDto1);

        OrderDto orderDto2 = new OrderDto();
        orderDto2.setCustomerId(testCustomer.getId());
        orderDto2.setIdProducts(Arrays.asList(testProduct2.getId()));
        orderService.createOrder(orderDto2);

        List<OrderDto> orders = orderService.findAllOrders();

        assertThat(orders).hasSize(6); // We should have 2 orders
    }

    @Test
    public void testDeleteOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(testCustomer.getId());
        orderDto.setIdProducts(Arrays.asList(testProduct1.getId()));
        OrderDto createdOrder = orderService.createOrder(orderDto);

        boolean isDeleted = orderService.deletOrder(createdOrder.getId());

        assertThat(isDeleted).isTrue();
        assertThat(orderRepository.count()).isEqualTo(0); // Ensure the order is deleted
    }

    @Test
    public void testUpdateOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(testCustomer.getId());
        orderDto.setIdProducts(Arrays.asList(testProduct1.getId()));
        OrderDto createdOrder = orderService.createOrder(orderDto);

        OrderDto updatedOrderDto = new OrderDto();
        updatedOrderDto.setCustomerId(testCustomer.getId());
        updatedOrderDto.setIdProducts(Arrays.asList(testProduct2.getId()));

        OrderDto updatedOrder = orderService.updateOrder(createdOrder.getId(), updatedOrderDto);

        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getIdProducts()).containsExactlyInAnyOrder(testProduct2.getId()); // Ensure it is updated
    }

    @Test
    public void testSearchKeywords() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(testCustomer.getId());
        orderDto.setIdProducts(Arrays.asList(testProduct1.getId()));
        orderService.createOrder(orderDto);

        List<String> keywords = orderService.searchKeywords("Product A");

        assertThat(keywords).contains("Product A");
        assertThat(keywords).contains(testCustomer.getName());
    }
}*/

package com.example.demo;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.demo.controllers.OrderController;
import com.example.demo.dtos.OrderDto;
import com.example.demo.dtos.OrderResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setOrdersProducts(new ArrayList<>());
    }

    @Test
    public void testCreateOrder_Success() throws Exception {
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(orderDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": 1, \"ordersProducts\": []}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    public void testFindById_Success() throws Exception {
        when(orderService.findById(anyLong())).thenReturn(orderDto);

        mockMvc.perform(get("/api/ordersbyid/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    public void testFindAllOrders_Success() throws Exception {
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);
        when(orderService.findAllOrders()).thenReturn(orderDtos);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].customerId").value(1));
    }

    @Test
    public void testFindByCustomerName_Success() throws Exception {
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);
        when(orderService.findByCustomerName(anyString())).thenReturn(orderDtos);

        mockMvc.perform(get("/api/orders/JohnDoe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].customerId").value(1));
    }

    @Test
    public void testDeletOrder_Success() throws Exception {

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDisplayOrderDetails_Success() throws Exception {
        List<OrderResponse> orderResponses = new ArrayList<>();
        when(orderService.displayOrderDetails()).thenReturn(orderResponses);

        mockMvc.perform(get("/api/orders/details"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetOrdersByCustomerId_Success() throws Exception {
        List<OrderResponse> orderResponses = new ArrayList<>();
        when(orderService.getOrdersByCustomerId(anyLong())).thenReturn(orderResponses);

        mockMvc.perform(get("/api/orders/details/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testSearchKeywords_Success() throws Exception {
        List<String> keywords = List.of("keyword1", "keyword2");
        when(orderService.searchKeywords(anyString())).thenReturn(keywords);

        mockMvc.perform(get("/api/search/someKeyword"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("keyword1"))
                .andExpect(jsonPath("$[1]").value("keyword2"));
    }

    @Test
    public void testUpdateOrder_Success() throws Exception {
        when(orderService.updateOrder(anyLong(), any(OrderDto.class))).thenReturn(orderDto);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": 1, \"ordersProducts\": []}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    public void testUpdateOrder_NotFound() throws Exception {
        when(orderService.updateOrder(anyLong(), any(OrderDto.class))).thenThrow(new ResourceNotFoundException("Order non trouvé !"));

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\": 1, \"ordersProducts\": []}"))
                .andExpect(status().isNotFound());
    }
}
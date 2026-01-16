package com.shoewarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoewarehouse.entity.Order;
import com.shoewarehouse.entity.OrderItem;
import com.shoewarehouse.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("600.00"));
    }

    @Test
    @WithMockUser
    void testGetAllOrders() throws Exception {
        List<Order> orders = Arrays.asList(order);
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/api/orders")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-001"));

        verify(orderService).getAllOrders();
    }

    @Test
    @WithMockUser
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"));

        verify(orderService).getOrderById(1L);
    }

    @Test
    @WithMockUser
    void testGetOrdersBySupplier() throws Exception {
        List<Order> orders = Arrays.asList(order);
        when(orderService.getOrdersBySupplier(1L)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/supplier/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-001"));

        verify(orderService).getOrdersBySupplier(1L);
    }

    @Test
    @WithMockUser
    void testGetOrdersByStatus() throws Exception {
        List<Order> orders = Arrays.asList(order);
        when(orderService.getOrdersByStatus(Order.OrderStatus.PENDING)).thenReturn(orders);

        mockMvc.perform(get("/api/orders/status/PENDING")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-001"));

        verify(orderService).getOrdersByStatus(Order.OrderStatus.PENDING);
    }

    @Test
    @WithMockUser
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"));

        verify(orderService).createOrder(any(Order.class));
    }

    @Test
    @WithMockUser
    void testUpdateOrder() throws Exception {
        when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(order);

        mockMvc.perform(put("/api/orders/1")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"));

        verify(orderService).updateOrder(eq(1L), any(Order.class));
    }

    @Test
    @WithMockUser
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());

        verify(orderService).deleteOrder(1L);
    }

    @Test
    @WithMockUser
    void testAddOrderItem() throws Exception {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(new BigDecimal("120.00"));

        when(orderService.addOrderItem(eq(1L), any(OrderItem.class))).thenReturn(orderItem);

        mockMvc.perform(post("/api/orders/1/items")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItem)))
                .andExpect(status().isCreated());

        verify(orderService).addOrderItem(eq(1L), any(OrderItem.class));
    }

    @Test
    @WithMockUser
    void testRemoveOrderItem() throws Exception {
        doNothing().when(orderService).removeOrderItem(1L);

        mockMvc.perform(delete("/api/orders/items/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isNoContent());

        verify(orderService).removeOrderItem(1L);
    }
}

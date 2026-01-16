package com.shoewarehouse.service;

import com.shoewarehouse.entity.*;
import com.shoewarehouse.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ShoeRepository shoeRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Supplier supplier;
    private Shoe shoe;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("ABC Suppliers");
        supplier.setEmail("info@abc.com");

        shoe = new Shoe();
        shoe.setId(1L);
        shoe.setModel("Air Max");
        shoe.setPrice(new BigDecimal("120.00"));

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(5);
        orderItem.setUnitPrice(new BigDecimal("120.00"));
        orderItem.setShoe(shoe);

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setSupplier(supplier);
        order.setOrderItems(Arrays.asList(orderItem));
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderNumber()).isEqualTo("ORD-001");
        verify(orderRepository).findAll();
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getOrderNumber()).isEqualTo("ORD-001");
        verify(orderRepository).findById(1L);
    }

    @Test
    void testGetOrdersBySupplier() {
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findBySupplierId(1L)).thenReturn(orders);

        List<Order> result = orderService.getOrdersBySupplier(1L);

        assertThat(result).hasSize(1);
        verify(orderRepository).findBySupplierId(1L);
    }

    @Test
    void testGetOrdersByStatus() {
        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findByStatus(Order.OrderStatus.PENDING)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByStatus(Order.OrderStatus.PENDING);

        assertThat(result).hasSize(1);
        verify(orderRepository).findByStatus(Order.OrderStatus.PENDING);
    }

    @Test
    void testCreateOrder() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(shoeRepository.findById(1L)).thenReturn(Optional.of(shoe));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertThat(result).isNotNull();
        // Order is saved twice: once with items, once with total amount
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testCreateOrderWithSupplierNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(order))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Supplier not found");
    }

    @Test
    void testUpdateOrder() {
        Order updatedOrder = new Order();
        updatedOrder.setStatus(Order.OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.updateOrder(1L, updatedOrder);

        assertThat(result).isNotNull();
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testUpdateOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrder(1L, order))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void testDeleteOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        doNothing().when(orderRepository).delete(order);

        orderService.deleteOrder(1L);

        verify(orderRepository).findById(1L);
        verify(orderRepository).delete(order);
    }

    @Test
    void testAddOrderItem() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(shoeRepository.findById(1L)).thenReturn(Optional.of(shoe));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList(orderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderItem result = orderService.addOrderItem(1L, orderItem);

        assertThat(result).isNotNull();
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    void testRemoveOrderItem() {
        // Set order on orderItem to avoid NullPointerException
        orderItem.setOrder(order);
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList());
        doNothing().when(orderItemRepository).delete(orderItem);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.removeOrderItem(1L);

        verify(orderItemRepository).delete(orderItem);
    }
}

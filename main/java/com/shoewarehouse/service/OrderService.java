package com.shoewarehouse.service;

import com.shoewarehouse.entity.Order;
import com.shoewarehouse.entity.OrderItem;
import com.shoewarehouse.entity.Shoe;
import com.shoewarehouse.repository.OrderRepository;
import com.shoewarehouse.repository.OrderItemRepository;
import com.shoewarehouse.repository.SupplierRepository;
import com.shoewarehouse.repository.ShoeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ShoeRepository shoeRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersBySupplier(Long supplierId) {
        return orderRepository.findBySupplierId(supplierId);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order createOrder(Order order) {
        if (order.getSupplier() != null && order.getSupplier().getId() != null) {
            order.setSupplier(supplierRepository.findById(order.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + order.getSupplier().getId())));
        }
        
        if (order.getOrderNumber() == null) {
            order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        
        if (order.getStatus() == null) {
            order.setStatus(Order.OrderStatus.PENDING);
        }
        
        // Set order reference on all order items before saving
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
                if (item.getShoe() != null && item.getShoe().getId() != null) {
                    Shoe shoe = shoeRepository.findById(item.getShoe().getId())
                            .orElseThrow(() -> new RuntimeException("Shoe not found with id: " + item.getShoe().getId()));
                    item.setShoe(shoe);
                    if (item.getUnitPrice() == null) {
                        item.setUnitPrice(shoe.getPrice());
                    }
                }
                if (item.getQuantity() == null) {
                    throw new RuntimeException("Quantity is required for order item");
                }
                item.calculateSubtotal();
            }
        }
        
        // Save order (cascade will save order items)
        Order savedOrder = orderRepository.save(order);
        
        // Calculate total amount after saving
        if (savedOrder.getOrderItems() != null && !savedOrder.getOrderItems().isEmpty()) {
            BigDecimal totalAmount = savedOrder.getOrderItems().stream()
                    .map(OrderItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            savedOrder.setTotalAmount(totalAmount);
            savedOrder = orderRepository.save(savedOrder);
        }
        
        return savedOrder;
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        if (orderDetails.getStatus() != null) {
            order.setStatus(orderDetails.getStatus());
        }
        if (orderDetails.getSupplier() != null && orderDetails.getSupplier().getId() != null) {
            order.setSupplier(supplierRepository.findById(orderDetails.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + orderDetails.getSupplier().getId())));
        }
        
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    public OrderItem addOrderItem(Long orderId, OrderItem orderItem) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        if (orderItem.getShoe() != null && orderItem.getShoe().getId() != null) {
            Shoe shoe = shoeRepository.findById(orderItem.getShoe().getId())
                    .orElseThrow(() -> new RuntimeException("Shoe not found with id: " + orderItem.getShoe().getId()));
            orderItem.setShoe(shoe);
            orderItem.setUnitPrice(shoe.getPrice());
        }
        
        orderItem.setOrder(order);
        orderItem.calculateSubtotal();
        OrderItem savedItem = orderItemRepository.save(orderItem);
        
        recalculateOrderTotal(orderId);
        
        return savedItem;
    }

    public void removeOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found with id: " + orderItemId));
        Long orderId = orderItem.getOrder().getId();
        orderItemRepository.delete(orderItem);
        recalculateOrderTotal(orderId);
    }

    private void recalculateOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
    }
}

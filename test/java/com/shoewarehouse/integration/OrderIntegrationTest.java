package com.shoewarehouse.integration;

import com.shoewarehouse.entity.*;
import com.shoewarehouse.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ShoeRepository shoeRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Brand brand;
    private Category category;
    private Supplier supplier;
    private Shoe shoe1;
    private Shoe shoe2;
    private Shoe shoe3;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setName("Nike");
        brand = brandRepository.save(brand);

        category = new Category();
        category.setName("Sneakers");
        category = categoryRepository.save(category);

        supplier = new Supplier();
        supplier.setName("Supplier ABC");
        supplier.setEmail("supplier@abc.com");
        supplier.setPhone("+1234567890");
        supplier = supplierRepository.save(supplier);

        shoe1 = createShoe("Air Max", "42", "Black", 10, new BigDecimal("120.00"));
        shoe2 = createShoe("Air Force", "43", "White", 15, new BigDecimal("100.00"));
        shoe3 = createShoe("Jordan", "44", "Red", 5, new BigDecimal("150.00"));
    }

    private Shoe createShoe(String model, String size, String color, Integer quantity, BigDecimal price) {
        Shoe shoe = new Shoe();
        shoe.setModel(model);
        shoe.setSize(size);
        shoe.setColor(color);
        shoe.setQuantity(quantity);
        shoe.setPrice(price);
        shoe.setBrand(brand);
        
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        shoe.setCategories(categories);
        
        return shoeRepository.save(shoe);
    }

    @Test
    void testCreateOrderWithMultipleItems() {
        Order order = new Order();
        order.setOrderNumber("ORD-001");
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setSupplier(supplier);

        List<OrderItem> items = new ArrayList<>();
        
        OrderItem item1 = new OrderItem();
        item1.setShoe(shoe1);
        item1.setQuantity(2);
        item1.setUnitPrice(shoe1.getPrice());
        item1.setOrder(order);
        items.add(item1);

        OrderItem item2 = new OrderItem();
        item2.setShoe(shoe2);
        item2.setQuantity(3);
        item2.setUnitPrice(shoe2.getPrice());
        item2.setOrder(order);
        items.add(item2);

        OrderItem item3 = new OrderItem();
        item3.setShoe(shoe3);
        item3.setQuantity(1);
        item3.setUnitPrice(shoe3.getPrice());
        item3.setOrder(order);
        items.add(item3);

        order.setOrderItems(items);

        Order savedOrder = orderRepository.save(order);
        
        for (OrderItem item : items) {
            orderItemRepository.save(item);
        }

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderItems()).hasSize(3);
        
        BigDecimal expectedTotal = shoe1.getPrice().multiply(BigDecimal.valueOf(2))
                .add(shoe2.getPrice().multiply(BigDecimal.valueOf(3)))
                .add(shoe3.getPrice().multiply(BigDecimal.valueOf(1)));
        
        savedOrder.setTotalAmount(expectedTotal);
        orderRepository.save(savedOrder);

        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getOrderItems()).hasSize(3);
        assertThat(foundOrder.getTotalAmount()).isEqualByComparingTo(expectedTotal);
    }

    @Test
    void testOrderItemSubtotalCalculation() {
        Order order = new Order();
        order.setOrderNumber("ORD-002");
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setSupplier(supplier);
        order = orderRepository.save(order);

        OrderItem item = new OrderItem();
        item.setShoe(shoe1);
        item.setQuantity(5);
        item.setUnitPrice(shoe1.getPrice());
        item.setOrder(order);
        item.calculateSubtotal();
        
        OrderItem savedItem = orderItemRepository.save(item);

        BigDecimal expectedSubtotal = shoe1.getPrice().multiply(BigDecimal.valueOf(5));
        assertThat(savedItem.getSubtotal()).isEqualByComparingTo(expectedSubtotal);
    }

    @Test
    void testFindOrdersBySupplier() {
        Order order1 = createOrder("ORD-003", supplier);
        Order order2 = createOrder("ORD-004", supplier);

        List<Order> orders = orderRepository.findBySupplierId(supplier.getId());
        assertThat(orders).hasSize(2);
    }

    @Test
    void testFindOrdersByStatus() {
        Order order1 = createOrder("ORD-005", supplier);
        order1.setStatus(Order.OrderStatus.COMPLETED);
        orderRepository.save(order1);

        Order order2 = createOrder("ORD-006", supplier);
        order2.setStatus(Order.OrderStatus.PENDING);
        orderRepository.save(order2);

        List<Order> completedOrders = orderRepository.findByStatus(Order.OrderStatus.COMPLETED);
        assertThat(completedOrders).hasSize(1);
        assertThat(completedOrders.get(0).getStatus()).isEqualTo(Order.OrderStatus.COMPLETED);
    }

    @Test
    void testDeleteOrderCascadesToOrderItems() {
        Order order = createOrder("ORD-007", supplier);
        
        OrderItem item1 = new OrderItem();
        item1.setShoe(shoe1);
        item1.setQuantity(1);
        item1.setUnitPrice(shoe1.getPrice());
        item1.setOrder(order);
        // Add to order's orderItems list for cascade to work
        order.getOrderItems().add(item1);
        orderRepository.save(order); // Cascade will save item1

        Long orderId = order.getId();
        Long itemId = item1.getId();

        orderRepository.delete(order);

        assertThat(orderRepository.findById(orderId)).isEmpty();
        assertThat(orderItemRepository.findById(itemId)).isEmpty();
    }

    private Order createOrder(String orderNumber, Supplier supplier) {
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setSupplier(supplier);
        return orderRepository.save(order);
    }
}

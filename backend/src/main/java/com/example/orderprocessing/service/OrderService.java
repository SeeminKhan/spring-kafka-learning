package com.example.orderprocessing.service;

import com.example.orderprocessing.dto.CreateOrderRequest;
import com.example.orderprocessing.event.OrderCreatedEvent;
import com.example.orderprocessing.model.Order;
import com.example.orderprocessing.model.OrderStatus;
import com.example.orderprocessing.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for order management.
 * 
 * Responsibilities:
 * - Create and persist new orders to MongoDB
 * - Publish OrderCreated events to Kafka for async processing
 * - Retrieve orders from database
 * - Update order status during processing
 * 
 * Uses Spring's dependency injection to wire:
 * - OrderRepository for MongoDB operations
 * - KafkaTemplate for publishing events to Kafka
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    
    @Value("${kafka.topic.orders}")  // Inject topic name from application.properties
    private String ordersTopic;

    /**
     * Constructor injection - Spring's recommended approach for dependency injection.
     * Makes dependencies explicit and enables easier testing.
     */
    public OrderService(OrderRepository orderRepository, 
                       KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Create a new order and publish an event to Kafka.
     * 
     * Process:
     * 1. Create Order entity with RECEIVED status
     * 2. Save to MongoDB (generates unique ID)
     * 3. Publish OrderCreatedEvent to Kafka topic
     * 4. Return saved order to caller
     * 
     * The orderId is used as the Kafka message key to ensure:
     * - Messages for the same order go to the same partition
     * - Processing order is maintained for each order
     */
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        logger.info("Creating order: {}", request);
        
        // Create and save order with RECEIVED status
        Order order = new Order(request.getProduct(), request.getQuantity());
        order.setStatus(OrderStatus.RECEIVED);
        Order savedOrder = orderRepository.save(order);
        
        logger.info("Order saved to MongoDB with ID: {}", savedOrder.getId());
        
        // Publish event to Kafka for asynchronous processing
        OrderCreatedEvent event = new OrderCreatedEvent(
            savedOrder.getId(),
            savedOrder.getProduct(),
            savedOrder.getQuantity()
        );
        
        // Use orderId as key for partition assignment and ordering guarantees
        kafkaTemplate.send(ordersTopic, savedOrder.getId(), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Successfully published OrderCreatedEvent for order: {}", 
                               savedOrder.getId());
                } else {
                    logger.error("Failed to publish OrderCreatedEvent for order: {}", 
                                savedOrder.getId(), ex);
                }
            });
        
        return savedOrder;
    }

    /**
     * Retrieve all orders sorted by creation time (newest first).
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Retrieve a specific order by ID.
     */
    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    /**
     * Update the status of an order.
     * Used by the Kafka consumer during order processing.
     */
    @Transactional
    public Order updateOrderStatus(String orderId, OrderStatus status) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            Order updatedOrder = orderRepository.save(order);
            logger.info("Updated order {} status to {}", orderId, status);
            return updatedOrder;
        } else {
            logger.warn("Order not found for status update: {}", orderId);
            throw new RuntimeException("Order not found: " + orderId);
        }
    }

    /**
     * Get count of orders by status.
     * Used for dashboard statistics.
     */
    public long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).size();
    }

    /**
     * Get total count of all orders.
     */
    public long getTotalOrderCount() {
        return orderRepository.count();
    }
}

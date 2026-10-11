package com.example.orderprocessing.repository;

import com.example.orderprocessing.model.Order;
import com.example.orderprocessing.model.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for Order entity.
 * 
 * Provides CRUD operations and custom query methods for orders.
 * Spring Data MongoDB automatically implements these methods.
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    /**
     * Find all orders with a specific status.
     * Useful for filtering orders by their processing state.
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Find all orders sorted by creation time in descending order.
     * Returns newest orders first.
     */
    List<Order> findAllByOrderByCreatedAtDesc();
}

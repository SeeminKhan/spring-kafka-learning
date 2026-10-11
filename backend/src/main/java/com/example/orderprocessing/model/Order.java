package com.example.orderprocessing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;

/**
 * Order entity representing an order document in MongoDB.
 * 
 * This document stores the complete order information including:
 * - Unique identifier (MongoDB ObjectId)
 * - Product name and quantity
 * - Processing status (RECEIVED, PROCESSING, PROCESSED, FAILED)
 * - Timestamps for audit trail
 */
@Document(collection = "orders")  // Map to MongoDB 'orders' collection
public class Order {

    @Id  // MongoDB document ID
    private String id;
    
    private String product;
    private int quantity;
    private OrderStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructors
    public Order() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = OrderStatus.RECEIVED;
    }

    public Order(String product, int quantity) {
        this();
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();  // Update timestamp when status changes
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

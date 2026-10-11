package com.example.orderprocessing.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Event published to Kafka when a new order is created.
 * 
 * This event contains all necessary information for asynchronous order processing.
 * The event is serialized to JSON and published to the 'orders' Kafka topic.
 * 
 * Event-driven architecture allows:
 * - Decoupling of order creation from processing
 * - Asynchronous processing workflow
 * - Scalability through Kafka partitions
 * - Reliability through Kafka's persistence
 */
public class OrderCreatedEvent {

    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("product")
    private String product;
    
    @JsonProperty("quantity")
    private int quantity;
    
    @JsonProperty("timestamp")
    private Instant timestamp;

    // Default constructor required for JSON deserialization
    public OrderCreatedEvent() {
        this.timestamp = Instant.now();
    }

    public OrderCreatedEvent(String orderId, String product, int quantity) {
        this();
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", product='" + product + '\'' +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}

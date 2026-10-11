package com.example.orderprocessing.model;

/**
 * Enum representing the lifecycle status of an order.
 * 
 * Status Flow:
 * 1. RECEIVED - Order created and saved to database
 * 2. PROCESSING - Kafka consumer picked up the order and started processing
 * 3. PROCESSED - Order processing completed successfully
 * 4. FAILED - Order processing failed (error handling)
 */
public enum OrderStatus {
    RECEIVED,    // Initial state when order is created
    PROCESSING,  // Order is being processed by Kafka consumer
    PROCESSED,   // Order processing completed successfully
    FAILED       // Order processing failed
}

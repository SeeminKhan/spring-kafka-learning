package com.example.orderprocessing.consumer;

import com.example.orderprocessing.event.OrderCreatedEvent;
import com.example.orderprocessing.model.OrderStatus;
import com.example.orderprocessing.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer that listens for OrderCreated events and processes them.
 * 
 * This consumer demonstrates:
 * - @KafkaListener annotation for consuming messages
 * - Consumer group configuration (order-processing-group)
 * - JSON deserialization of Kafka messages
 * - Asynchronous order processing workflow
 * - Error handling and logging
 * 
 * Processing Flow:
 * 1. Receive OrderCreatedEvent from Kafka
 * 2. Update order status to PROCESSING
 * 3. Simulate processing work
 * 4. Update order status to PROCESSED
 * 5. Handle errors by setting status to FAILED
 * 
 * Idempotency Note:
 * This implementation uses a simple approach where the consumer processes
 * each message as received. In a production system, you would need to:
 * - Check if the order has already been processed (status != RECEIVED)
 * - Use a unique processing ID or version number
 * - Implement distributed locking for critical sections
 * 
 * The current approach provides at-least-once processing semantics.
 * Kafka's offset management ensures messages aren't lost, but they may
 * be redelivered if the consumer fails before committing the offset.
 */
@Component
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Listen to the 'orders' topic and process incoming events.
     * 
     * Kafka Configuration:
     * - Topic: ${kafka.topic.orders} (configured in application.properties)
     * - Consumer Group: order-processing-group (allows scaling consumers)
     * - Auto-offset-commit: enabled (offsets committed automatically)
     * - Deserialization: JSON to OrderCreatedEvent object
     * 
     * The @Payload annotation tells Spring to deserialize the message value.
     * The @Header annotation extracts message metadata like partition and offset.
     */
    @KafkaListener(
        topics = "${kafka.topic.orders}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderEvent(
        @Payload OrderCreatedEvent event,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset
    ) {
        logger.info("Received OrderCreatedEvent from partition {} offset {}: {}", 
                   partition, offset, event);

        try {
            // Step 1: Update order status to PROCESSING
            orderService.updateOrderStatus(event.getOrderId(), OrderStatus.PROCESSING);
            logger.info("Order {} status updated to PROCESSING", event.getOrderId());

            // Step 2: Simulate order processing work
            // In a real system, this might involve:
            // - Inventory validation
            // - Payment processing
            // - Shipping coordination
            // - External API calls
            processOrder(event);

            // Step 3: Update order status to PROCESSED
            orderService.updateOrderStatus(event.getOrderId(), OrderStatus.PROCESSED);
            logger.info("Order {} successfully processed", event.getOrderId());

        } catch (Exception e) {
            // Handle processing errors
            logger.error("Failed to process order {}: {}", event.getOrderId(), e.getMessage(), e);
            
            try {
                // Update order status to FAILED
                orderService.updateOrderStatus(event.getOrderId(), OrderStatus.FAILED);
            } catch (Exception updateException) {
                logger.error("Failed to update order {} status to FAILED", 
                           event.getOrderId(), updateException);
            }
        }
    }

    /**
     * Simulate order processing with a delay.
     * 
     * This represents the work that happens asynchronously:
     * - Business logic validation
     * - External service calls
     * - Data transformations
     * 
     * In this demo, we just sleep for 2 seconds to show the status
     * progression from RECEIVED -> PROCESSING -> PROCESSED.
     */
    private void processOrder(OrderCreatedEvent event) throws InterruptedException {
        logger.info("Processing order {} for {} x{}", 
                   event.getOrderId(), event.getProduct(), event.getQuantity());
        
        // Simulate processing time (2 seconds)
        Thread.sleep(2000);
        
        logger.info("Order {} processing completed", event.getOrderId());
    }
}

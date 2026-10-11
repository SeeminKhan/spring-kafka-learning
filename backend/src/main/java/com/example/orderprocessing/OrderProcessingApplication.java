package com.example.orderprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main Spring Boot application class for Order Processing System.
 * 
 * This application demonstrates:
 * - REST API endpoints for order management
 * - Event-driven processing using Apache Kafka
 * - MongoDB for persistent data storage
 * - Asynchronous order processing workflow
 */
@SpringBootApplication
@EnableKafka  // Enable Kafka support for producers and consumers
public class OrderProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessingApplication.class, args);
    }
}

package com.example.orderprocessing.config;

import com.example.orderprocessing.event.OrderCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration for producers and consumers.
 * 
 * This configuration sets up:
 * - Producer for publishing OrderCreatedEvent messages
 * - Consumer for receiving OrderCreatedEvent messages
 * - JSON serialization/deserialization
 * - Consumer group and offset management
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    /**
     * Producer configuration for publishing events to Kafka.
     * 
     * Key settings:
     * - ACKS_CONFIG=all: Wait for all replicas to acknowledge (durability)
     * - RETRIES_CONFIG=3: Retry failed sends up to 3 times
     * - Key serializer: String (orderId)
     * - Value serializer: JSON (OrderCreatedEvent)
     */
    @Bean
    public ProducerFactory<String, OrderCreatedEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");  // Wait for all replicas
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);   // Retry up to 3 times
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * KafkaTemplate for sending messages to Kafka.
     * 
     * This is the main interface for publishing events.
     * Spring Boot auto-configuration would create this bean,
     * but we define it explicitly for clarity.
     */
    @Bean
    public KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Consumer configuration for receiving events from Kafka.
     * 
     * Key settings:
     * - Consumer group: Allows multiple consumers to share the load
     * - AUTO_OFFSET_RESET_CONFIG=earliest: Start from the beginning if no offset exists
     * - Key deserializer: String
     * - Value deserializer: JSON with trusted packages
     * - ENABLE_AUTO_COMMIT_CONFIG=true: Automatically commit offsets
     */
    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");  // Trust all packages for deserialization
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrderCreatedEvent.class);
        
        return new DefaultKafkaConsumerFactory<>(
            configProps,
            new StringDeserializer(),
            new JsonDeserializer<>(OrderCreatedEvent.class, false)  // false = don't use type headers
        );
    }

    /**
     * Kafka listener container factory for @KafkaListener methods.
     * 
     * This factory creates the containers that host Kafka consumer threads.
     * Each @KafkaListener method runs in a container created by this factory.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

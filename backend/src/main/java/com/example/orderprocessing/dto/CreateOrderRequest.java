package com.example.orderprocessing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for creating a new order.
 * 
 * Includes validation annotations to ensure:
 * - Product name is provided and not empty
 * - Quantity is positive
 */
public class CreateOrderRequest {

    @NotBlank(message = "Product name is required")
    private String product;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Constructors
    public CreateOrderRequest() {}

    public CreateOrderRequest(String product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

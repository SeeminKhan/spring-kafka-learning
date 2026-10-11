package com.example.orderprocessing.controller;

import com.example.orderprocessing.dto.CreateOrderRequest;
import com.example.orderprocessing.dto.ErrorResponse;
import com.example.orderprocessing.model.Order;
import com.example.orderprocessing.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for order management endpoints.
 * 
 * Provides HTTP endpoints for:
 * - Creating new orders (POST)
 * - Retrieving all orders (GET)
 * - Retrieving a specific order by ID (GET)
 * 
 * All endpoints are prefixed with /api/orders
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    /**
     * Constructor injection of OrderService.
     * Spring automatically injects the service bean.
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order.
     * 
     * POST /api/orders
     * 
     * Request Body:
     * {
     *   "product": "Laptop",
     *   "quantity": 2
     * }
     * 
     * Response: 201 Created with Order object
     * 
     * The @Valid annotation triggers validation of the request body.
     * Invalid requests return 400 Bad Request with error details.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        logger.info("Received create order request: {}", request);
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    /**
     * Get all orders sorted by creation time (newest first).
     * 
     * GET /api/orders
     * 
     * Response: 200 OK with array of Order objects
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("Fetching all orders");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Get a specific order by ID.
     * 
     * GET /api/orders/{id}
     * 
     * Response: 200 OK with Order object, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        logger.info("Fetching order with ID: {}", id);
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Exception handler for validation errors.
     * 
     * Returns 400 Bad Request with detailed error messages
     * when request validation fails.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Invalid request parameters",
                errors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Generic exception handler for unexpected errors.
     * 
     * Returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred: " + ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

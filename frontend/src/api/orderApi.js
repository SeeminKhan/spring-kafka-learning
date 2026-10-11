/**
 * API client for Order Processing backend.
 * 
 * Handles all HTTP communication with the Spring Boot REST API.
 * Base URL is configured to match the backend server.
 */

const API_BASE_URL = 'http://localhost:8080/api/orders'

/**
 * Fetch all orders from the backend.
 * 
 * GET /api/orders
 * 
 * Returns: Array of order objects
 */
export async function fetchOrders() {
  const response = await fetch(API_BASE_URL)
  
  if (!response.ok) {
    throw new Error(`Failed to fetch orders: ${response.statusText}`)
  }
  
  return response.json()
}

/**
 * Fetch a specific order by ID.
 * 
 * GET /api/orders/{id}
 * 
 * Returns: Order object
 */
export async function fetchOrderById(id) {
  const response = await fetch(`${API_BASE_URL}/${id}`)
  
  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Order not found')
    }
    throw new Error(`Failed to fetch order: ${response.statusText}`)
  }
  
  return response.json()
}

/**
 * Create a new order.
 * 
 * POST /api/orders
 * Body: { product: string, quantity: number }
 * 
 * Returns: Created order object
 */
export async function createOrder(orderData) {
  const response = await fetch(API_BASE_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(orderData),
  })
  
  if (!response.ok) {
    // Parse error response if available
    try {
      const errorData = await response.json()
      throw new Error(errorData.message || `Failed to create order: ${response.statusText}`)
    } catch (parseError) {
      throw new Error(`Failed to create order: ${response.statusText}`)
    }
  }
  
  return response.json()
}

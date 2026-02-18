# API-PILOT — Spring Boot Microservices

A microservices project built with Spring Boot, Spring Cloud, and Netflix Eureka.

## Architecture

```
                        ┌─────────────────┐
                        │   API Gateway   │
                        │   (Port 8080)   │
                        └────────┬────────┘
                                 │
                    ┌────────────▼────────────┐
                    │     Eureka Server       │
                    │     (Port 8761)         │
                    └────────────┬────────────┘
                                 │
               ┌─────────────────┴──────────────────┐
               │                                    │
    ┌──────────▼──────────┐             ┌───────────▼───────────┐
    │   Product Service   │◄────Feign───│    Order Service      │
    │   (Port 8081)       │             │    (Port 8082)        │
    └─────────────────────┘             └───────────────────────┘
```

## Services

| Service | Port | Description |
|---|---|---|
| Eureka Server | 8761 | Service registry and discovery |
| API Gateway | 8080 | Single entry point for all requests |
| Product Service | 8081 | Manages products and stock |
| Order Service | 8082 | Manages orders, calls Product Service via Feign |

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.2**
- **Spring Cloud 2024.0.1**
- **Spring Cloud Gateway**
- **Netflix Eureka** (Service Discovery)
- **OpenFeign** (Inter-service communication)
- **Spring Data JPA**
- **MariaDB** (Product Service & Order Service)
- **Lombok**

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- MySQL / MariaDB running locally
- Git

### Clone the Repository

```bash
git clone https://github.com/tharidul/API-PILOT.git
cd API-PILOT
```

### Database Setup

```sql
CREATE DATABASE productdb;
CREATE DATABASE orderdb;
```

### Start Services (in order)

```bash
# 1. Eureka Server
cd eureka-server
./mvnw spring-boot:run

# 2. Product Service
cd ../product-service
./mvnw spring-boot:run

# 3. Order Service
cd ../order-service
./mvnw spring-boot:run

# 4. API Gateway
cd ../api-gateway
./mvnw spring-boot:run
```

### Verify Services

Open Eureka Dashboard: `http://localhost:8761`

You should see `API-GATEWAY`, `PRODUCT-SERVICE`, and `ORDER-SERVICE` all registered and UP.

---

## API Endpoints

All requests go through the **API Gateway on port 8080**.

All responses follow this standard format:

**Success:**
```json
{
    "success": true,
    "message": "Success",
    "data": { }
}
```

**Error:**
```json
{
    "success": false,
    "message": "Error description",
    "data": null
}
```

---

### Product Service

| Method | URL | Description |
|---|---|---|
| `POST` | `/product-service/products` | Create a product |
| `GET` | `/product-service/products` | Get all products |
| `GET` | `/product-service/products/{id}` | Get product by ID |
| `PUT` | `/product-service/products/{id}` | Update a product |
| `DELETE` | `/product-service/products/{id}` | Delete a product |
| `PUT` | `/product-service/products/{id}/reduce-stock` | Reduce product stock |

#### Create Product

```
POST /product-service/products
```
```json
{
    "name": "Gaming Mouse",
    "price": 1200.0,
    "description": "High precision gaming mouse",
    "stock": 10
}
```

#### Update Product

```
PUT /product-service/products/1
```
```json
{
    "name": "Gaming Mouse Pro",
    "price": 1500.0,
    "description": "Upgraded gaming mouse",
    "stock": 20
}
```

---

### Order Service

| Method | URL | Description |
|---|---|---|
| `POST` | `/order-service/orders` | Create an order |
| `GET` | `/order-service/orders` | Get all orders |
| `GET` | `/order-service/orders/{id}` | Get order by ID |
| `DELETE` | `/order-service/orders/{id}` | Delete an order |

#### Create Order

```
POST /order-service/orders
```
```json
{
    "productId": 1,
    "quantity": 2
}
```

When an order is created, the system will:
1. Validate the product exists in Product Service
2. Check if stock is sufficient
3. Calculate total price automatically (`price × quantity`)
4. Reduce product stock
5. Save and return the order

---

## Project Structure

```
API-PILOT/
├── api-gateway/
│   └── src/main/resources/application.properties
├── eureka-server/
│   └── src/main/resources/application.properties
├── product-service/
│   └── src/main/java/live/lkml/productservice/
│       ├── controller/ProductController.java
│       ├── service/ProductService.java
│       ├── repository/ProductRepository.java
│       ├── entity/Product.java
│       └── dto/
│           ├── request/ProductRequestDTO.java
│           └── response/ProductResponseDTO.java
└── order-service/
    └── src/main/java/live/lkml/orderservice/
        ├── controller/OrderController.java
        ├── service/OrderService.java
        ├── repository/OrderRepository.java
        ├── entity/Order.java
        ├── client/ProductClient.java
        └── dto/
            ├── request/OrderRequestDTO.java
            └── response/
                ├── OrderResponseDTO.java
                └── ProductClientResponse.java
```

---

## Inter-Service Communication

Order Service calls Product Service directly via **OpenFeign** using Eureka service discovery.

A dedicated `/internal/{id}` endpoint is used for Feign calls to return raw product data, while the public `/products/{id}` returns the standard wrapped response:

```
External clients  →  /products/{id}           → wrapped ProductResponseDTO
Internal Feign    →  /products/internal/{id}  → raw Product (no wrapper)
```

---

## Business Rules

- Duplicate product names are not allowed
- Orders cannot be placed if stock is insufficient
- Stock is automatically reduced when an order is placed
- Total price is auto-calculated (`price × quantity`)

---

## Author

**Tharidul** — [github.com/tharidul](https://github.com/tharidul)

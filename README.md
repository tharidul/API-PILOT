# Spring Microservices Pilot

A pilot project to explore and test microservice architecture using Spring Boot and Spring Cloud.

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
- **Netflix Eureka**
- **OpenFeign**
- **Spring Data JPA**
- **MariaDB**
- **Lombok**

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- MariaDB running locally

### Database Setup

```sql
CREATE DATABASE productdb;
CREATE DATABASE orderdb;
```

### Start Services (in order)

```bash
# 1. Eureka Server
cd eureka-server && ./mvnw spring-boot:run

# 2. Product Service
cd product-service && ./mvnw spring-boot:run

# 3. Order Service
cd order-service && ./mvnw spring-boot:run

# 4. API Gateway
cd api-gateway && ./mvnw spring-boot:run
```

Verify at Eureka Dashboard: `http://localhost:8761`

---

## API Endpoints

All requests go through the API Gateway on port `8080`.

### Standard Response Format

```json
{
    "success": true,
    "message": "Success",
    "data": { }
}
```

### Product Service

| Method | URL | Description |
|---|---|---|
| `POST` | `/product-service/products` | Create a product |
| `GET` | `/product-service/products` | Get all products |
| `GET` | `/product-service/products/{id}` | Get product by ID |
| `PUT` | `/product-service/products/{id}` | Update a product |
| `DELETE` | `/product-service/products/{id}` | Delete a product |

#### Create Product
```json
{
    "name": "Gaming Mouse",
    "price": 1200.0,
    "description": "High precision gaming mouse",
    "stock": 10
}
```

### Order Service

| Method | URL | Description |
|---|---|---|
| `POST` | `/order-service/orders` | Create an order |
| `GET` | `/order-service/orders` | Get all orders |
| `GET` | `/order-service/orders/{id}` | Get order by ID |
| `DELETE` | `/order-service/orders/{id}` | Delete an order |

#### Create Order
```json
{
    "productId": 1,
    "quantity": 2
}
```

When an order is created:
1. Validates product exists
2. Checks stock is sufficient
3. Calculates total price (`price × quantity`)
4. Reduces product stock
5. Saves and returns the order

---

## Business Rules

- Duplicate product names are not allowed
- Orders cannot be placed if stock is insufficient
- Stock is automatically reduced when an order is placed

---

## Inter-Service Communication

Order Service calls Product Service via **OpenFeign** using Eureka service discovery.

A dedicated `/internal/{id}` endpoint is used for Feign calls returning raw product data, while the public endpoint returns the standard wrapped response.

---

## Author

**Tharidu Lakmal** — [github.com/tharidul](https://github.com/tharidul)

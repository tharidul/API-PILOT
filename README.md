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
- **MariaDB** (Order Service)
- **MySQL** (Product Service)
- **Lombok**

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

Open Eureka Dashboard: http://localhost:8761

You should see `API-GATEWAY`, `PRODUCT-SERVICE`, and `ORDER-SERVICE` all registered.

---

## API Endpoints

All requests go through the **API Gateway on port 8080**.

### Product Service

| Method | URL | Description |
|---|---|---|
| `POST` | `/product-service/products` | Create a product |
| `GET` | `/product-service/products` | Get all products |
| `GET` | `/product-service/products/{id}` | Get product by ID |
| `PUT` | `/product-service/products/{id}` | Update a product |
| `DELETE` | `/product-service/products/{id}` | Delete a product |
| `PUT` | `/product-service/products/{id}/reduce-stock` | Reduce product stock |

#### Create Product Example

```json
POST /product-service/products
{
    "name": "Gaming Mouse",
    "price": 1200.0,
    "description": "Gaming mouse",
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

#### Create Order Example

```json
POST /order-service/orders
{
    "productId": 1,
    "quantity": 2
}
```

When an order is created, the system will:
1. Validate the product exists in Product Service
2. Calculate total price automatically
3. Reduce product stock
4. Save and return the order

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
│       └── entity/Product.java
└── order-service/
    └── src/main/java/live/lkml/orderservice/
        ├── controller/OrderController.java
        ├── service/OrderService.java
        ├── repository/OrderRepository.java
        ├── entity/Order.java
        ├── client/ProductClient.java
        └── dto/
            ├── request/OrderRequest.java
            └── response/ProductResponse.java
```

## Inter-Service Communication

Order Service calls Product Service directly via **OpenFeign** (not through the gateway):

```
Order Service → Feign → PRODUCT-SERVICE (Eureka load-balanced)
```

This means services talk to each other directly using service discovery — bypassing the gateway for internal communication.

## Configuration

### API Gateway (`application.properties`)

```properties
spring.application.name=api-gateway
server.port=8080
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

spring.cloud.gateway.routes[0].id=product-service
spring.cloud.gateway.routes[0].uri=lb://PRODUCT-SERVICE
spring.cloud.gateway.routes[0].predicates[0]=Path=/product-service/**
spring.cloud.gateway.routes[0].filters[0]=StripPrefix=1

spring.cloud.gateway.routes[1].id=order-service
spring.cloud.gateway.routes[1].uri=lb://ORDER-SERVICE
spring.cloud.gateway.routes[1].predicates[0]=Path=/order-service/**
spring.cloud.gateway.routes[1].filters[0]=StripPrefix=1
```

---

## Author

**Tharidul** — [github.com/tharidul](https://github.com/tharidul)

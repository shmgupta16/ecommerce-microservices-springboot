# Ecommerce Backend

Spring Boot backend based on the provided ecommerce PRD and HLD.

## Features

- User registration, login, profile management, and password reset token creation
- JWT-based authentication and session-style bearer token handling
- Product categories, product details, keyword search, and catalog browsing
- Cart add/update/remove/review APIs
- Checkout flow that creates an order and payment record
- Order history and tracking status APIs
- Payment receipt endpoint
- Event publisher abstraction for Kafka-ready domain events
- Docker Compose for MySQL, Kafka, Redis, MongoDB, Elasticsearch, and Kong-friendly local architecture

## Run Locally

The default profile uses H2 so the API can start without external services:

```bash
mvn spring-boot:run
```

API base URL:

```text
http://localhost:8080/api
```

H2 console:

```text
http://localhost:8080/h2-console
```

## Useful Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`
- `PUT /api/users/me`
- `POST /api/users/password-reset`
- `GET /api/categories`
- `GET /api/products`
- `GET /api/products/search?q=phone`
- `GET /api/products/{id}`
- `GET /api/cart`
- `POST /api/cart/items`
- `PUT /api/cart/items/{productId}`
- `DELETE /api/cart/items/{productId}`
- `POST /api/orders/checkout`
- `GET /api/orders`
- `GET /api/orders/{id}/tracking`
- `GET /api/payments/{paymentId}/receipt`

## Architecture Notes

This project is implemented as a modular monolith for local development speed. Packages map directly to the HLD microservices:

- `user`: User Management Service
- `catalog`: Product Catalog Service
- `cart`: Cart Service
- `order`: Order Management Service
- `payment`: Payment Service
- `notification`: Notification Service placeholder via event publishing
- `common`: cross-cutting API, events, and security helpers

The `DomainEventPublisher` currently logs events and can be switched to Kafka by enabling the `kafka` profile and wiring `KafkaTemplate`.

# ecommerce-microservices-springboot

Modular Spring Boot ecommerce backend with JWT authentication, JPA persistence, and event-publishing abstractions.

This Spring Boot backend is based on the provided ecommerce PRD and HLD.

## Features

- User registration, login, profile management, and password reset token creation
- JWT-based authentication and session-style bearer token handling
- Product categories, product details, keyword search, and catalog browsing
- Cart add/update/remove/review APIs
- Checkout flow that creates an order and payment record
- Order history and tracking status APIs
- Payment receipt endpoint
- Event publisher abstraction for Kafka-ready domain events
- Docker Compose for optional local infrastructure: MySQL, Kafka, Redis, MongoDB, and Elasticsearch
- OpenAPI documentation through Swagger UI
- Health endpoint through Spring Boot Actuator

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

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

Health endpoint:

```text
http://localhost:8080/actuator/health
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
- `POST /api/admin/catalog/categories`
- `POST /api/admin/catalog/products`
- `PUT /api/admin/catalog/products/{productId}`
- `PATCH /api/admin/catalog/products/{productId}/inventory`
- `GET /api/cart`
- `POST /api/cart/items`
- `PUT /api/cart/items/{productId}`
- `DELETE /api/cart/items/{productId}`
- `POST /api/orders/checkout`
- `GET /api/orders`
- `GET /api/orders/{id}/tracking`
- `POST /api/payments/checkout-session`
- `GET /api/payments/{paymentId}/receipt`

## Originality Note

This project was refined with high-level reference from `geeky-sanjay/shop`, but no source code was copied. See `REFERENCE_NOTES.md` for the reference and originality notes.

## Development Admin

For local demonstration, the default profile seeds an admin user:

```text
Email: admin@shop.local
Password: Admin@12345
```

Change these values in `application.yml` before using the project outside a local development environment.

## Architecture Notes

This project is implemented as a modular monolith for local development speed. Packages map directly to the HLD microservices:

- `user`: User Management Service
- `catalog`: Product Catalog Service
- `cart`: Cart Service
- `order`: Order Management Service
- `payment`: Payment Service
- `notification`: Planned future Notification Service, to be implemented through event publishing
- `common`: cross-cutting API, events, and security helpers

The `DomainEventPublisher` currently logs events and can be switched to Kafka by enabling the `kafka` profile and wiring `KafkaTemplate`.

The services defined in `docker-compose.yml` are local support services for future integrations. The current runtime only requires the Spring Boot app and its configured database profile.

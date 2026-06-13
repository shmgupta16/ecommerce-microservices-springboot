# Reference and Originality Notes

This project was refined with high-level reference from:

```text
https://github.com/geeky-sanjay/shop
```

## What Was Referenced

- General ecommerce backend feature coverage.
- MVC-style separation of controllers, services, repositories, and domain models.
- Product, cart, order, and payment workflow ideas.
- The idea of a checkout-session style payment flow.
- Documentation topics such as features, limitations, and improvement scope.

## What Was Not Copied

- No source files were copied.
- No package names, class implementations, endpoint implementations, or business logic were copied.
- This project keeps its own modular microservices-oriented package structure.
- Authentication uses this project's existing JWT implementation.
- Payment checkout sessions are simulated through an original abstraction and do not copy Stripe integration code.

## Original Direction

The implementation is designed as a modular Spring Boot backend aligned with the submitted PRD and HLD. It keeps package-level boundaries for user, catalog, cart, order, payment, common security, and event publishing so the system can later be split into true microservices.

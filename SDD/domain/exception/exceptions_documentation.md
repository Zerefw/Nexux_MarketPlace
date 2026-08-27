# Domain Exceptions Documentation

## Overview
The `exception` package contains custom exception classes used exclusively within the Domain Layer. In Domain-Driven Design (DDD), the domain must remain completely isolated from infrastructure, frameworks, or delivery mechanisms (like HTTP status codes). 

## Classes

### `DomainException`
- **Type**: `RuntimeException`
- **Purpose**: Acts as the primary base exception for any business rule violation or invariant failure within NexusMarket.
- **Usage Context**: 
  - Thrown during object instantiation (e.g., inside factory methods) if required parameters are missing or invalid.
  - Thrown when attempting an invalid state transition (e.g., trying to mark a `CART` order directly as `SHIPPED`).
  - Thrown when a business constraint is violated (e.g., insufficient stock available for an order).
- **Design Decision**: Extending `RuntimeException` avoids checked exception boilerplate, keeping business logic clean. It will be caught globally by the Application/Infrastructure layer (e.g., a `@RestControllerAdvice` in Spring) and translated into the appropriate API error response (like a 400 Bad Request or 409 Conflict).

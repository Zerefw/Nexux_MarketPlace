# Domain Exceptions Documentation

## Overview
The `exception` package contains custom exception classes exclusively used in the Domain Layer. According to Domain-Driven Design (DDD), the business core must remain completely isolated from infrastructure implementations, frameworks, and delivery mechanisms (such as HTTP status codes).

## Implemented Classes

### `DomainException`
- **Type**: `RuntimeException`
- **Purpose**: Acts as the primary base exception to indicate any violation of business rules or the breach of invariants within the 30 services defined for the NexusMarket system.
- **Contexts of Use**:
  - Thrown during the instantiation of domain objects (e.g., within factory methods) if the provided parameters are invalid or null.
  - Thrown on invalid state transitions (e.g., attempting to transition an order in `CART` state directly to the logistical `SHIPPED` state).
  - Thrown upon violating business constraints (e.g., insufficient physical inventory balance to cover a reservation, or attempting to activate a non-existent user).
- **Architectural Design Decision**: Extending `RuntimeException` prevents the propagation of checked exceptions, preserving the readability and cleanliness of business logic. These exceptions are globally intercepted by the application or infrastructure layer (e.g., through global exception handlers) to be translated into standardized error responses.

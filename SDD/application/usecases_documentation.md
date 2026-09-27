# Application Layer (Use Cases) Documentation

## Overview
The `usecase` package acts as the orchestrator of the system. It implements the **Input Ports** defined in the Hexagonal Architecture and coordinates the **Domain Entities** with the **Output Ports** (Adapters). Use cases contain application-specific business rules, but no pure domain logic (which is delegated to entities).

## Implemented Services

### `RegisterUserService`
- **Purpose**: Manages the onboarding of new users (Buyers and Sellers).
- **Transactionality**: Uses Spring's `@Transactional` to ensure that saving the base `User` entity and the specific profile (`BuyerProfile` or `SellerProfile`) occurs atomically. If one fails, the entire transaction rolls back.
- **Port Usage**:
  - `UserRepositoryPort`: To check for email/document uniqueness and save the user.
  - `BuyerProfileRepositoryPort` / `SellerProfileRepositoryPort`: To save the respective profiles.
  - `PasswordEncoderPort`: To delegate password encryption without tying the logic directly to Spring Security's BCrypt.

### `ProductCatalogService`
- **Purpose**: Manages the e-commerce product catalog.
- **Transactionality**: Entirely `@Transactional` to prevent partial data writes.
- **Validations**: 
  - Prevents the creation of products with duplicate SKUs via the `ProductRepositoryPort.existsBySku()` check.
  - Automatically parses String types into strict Domain Enums (`ProductType`).
- **Domain Delegation**: Calls the strict Factory Method `Product.create()` and `addVariant()` to ensure the Domain Entity is valid before it even reaches the persistence adapter.

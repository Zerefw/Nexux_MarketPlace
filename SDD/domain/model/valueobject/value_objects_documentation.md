# Value Objects & Enums Documentation

## Overview
The `valueobject` package contains classes that represent descriptive aspects of the domain with no conceptual identity. In DDD, Value Objects (VOs) are immutable, self-validating, and compared by their properties, not by reference. We heavily leverage Lombok's `@Value` to enforce immutability.

## Value Objects

### `Money`
- **Responsibility**: Represents a monetary amount combined with its currency.
- **Attributes**: `amount` (BigDecimal), `currency` (String).
- **Behaviors**: Contains domain logic for safe addition (`add`) and multiplication (`multiply`). It actively prevents mathematical operations between different currencies, throwing a `DomainException`.

### `Email`
- **Responsibility**: Encapsulates email validation.
- **Attributes**: `address` (String).
- **Behaviors**: Guarantees that any instantiated `Email` object contains an `@` symbol and is not empty.

### `Sku` (Stock Keeping Unit)
- **Responsibility**: Represents a unique identifier for physical and digital products.
- **Attributes**: `code` (String).
- **Behaviors**: Self-validates against null or blank values upon creation.

### `StockLocation`
- **Responsibility**: Represents a physical location inside a warehouse.
- **Attributes**: `aisle`, `rack`, `shelf`.
- **Behaviors**: Ensures complete geographical coordinates within a facility.

## Enumerations (Business States & Types)

### `UserRole`
- **Values**: `BUYER`, `SELLER`, `LOGISTICS_OPERATOR`, `ADMIN`, `SUPERVISOR`.
- **Purpose**: Defines the strict authorization boundaries within the NexusMarket platform, as established in the Functional Specification.

### `OrderStatus`
- **Values**: `CART`, `PENDING_PAYMENT`, `PAID`, `SHIPPED`, `DELIVERED_FINALIZED`.
- **Purpose**: Represents the strict lifecycle of an order. Ensures an order cannot bypass logical steps.

### `ProductType`
- **Values**: `PHYSICAL`, `DIGITAL`.
- **Purpose**: Distinguishes fulfillment logic. Physical products require warehouse stock and shipping, while digital products bypass the logistics chain.

### `WarehouseType`
- **Values**: `MARKETPLACE`, `SELLER`.
- **Purpose**: Identifies whether a storage facility belongs centrally to NexusMarket or independently to a specific seller.

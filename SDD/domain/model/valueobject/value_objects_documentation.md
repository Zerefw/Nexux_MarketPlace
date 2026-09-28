# Value Objects and Enumerations Documentation

## Overview
The `valueobject` package houses classes that represent descriptive aspects of the domain devoid of conceptual identity. In DDD, Value Objects (VOs) are immutable, self-validate upon instantiation, and are strictly compared by the value of their properties and not by memory references.

## Implemented Value Objects

### `Money`
- **Responsibility**: Represents an indivisible economic amount of its currency.
- **Attributes**: `amount` (BigDecimal), `currency` (String).
- **Immersed Logic**: Defines safe arithmetic operations (`add`, `multiply`). Strictly prevents operations between disparate currencies by throwing domain exceptions.

### `Email`
- **Responsibility**: Encapsulates and validates the format of email addresses.
- **Attributes**: `address` (String).
- **Immersed Logic**: Ensures that the object contains a valid formal structure before permitting its use in the domain.

### `Sku` (Stock Keeping Unit)
- **Responsibility**: Unique referential identifier for catalog and inventory products.
- **Attributes**: `code` (String).
- **Immersed Logic**: Prevents the creation of empty or null codes, ensuring traceability.

### `StockLocation`
- **Responsibility**: Three-dimensional physical location within a logistical facility.
- **Attributes**: `aisle`, `rack`, `shelf`.
- **Immersed Logic**: Guarantees that the coordinates are complete.

## Enumerations (Business States and Types)

### `UserRole`
- **Values**: `BUYER`, `SELLER`, `LOGISTICS_OPERATOR`, `ADMIN`, `SUPERVISOR`.
- **Purpose**: Demarcates the boundaries of authorization and privileges within the platform.

### `OrderStatus`
- **Values**: `CART`, `PENDING_PAYMENT`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELLED`.
- **Purpose**: Stipulates the strict lifecycle of orders. Prevents the evasion of transactional or operational steps.

### `ProductType`
- **Values**: `PHYSICAL`, `DIGITAL`.
- **Purpose**: Differentiates logistical fulfillment logic. Physical products demand warehouse management, whereas digital ones omit it.

### `WarehouseType`
- **Values**: `MARKETPLACE`, `SELLER`.
- **Purpose**: Identifies the operational ownership of the storage facility.

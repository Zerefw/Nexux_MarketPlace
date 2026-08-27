# Entities Documentation

## Overview
The `entity` package contains domain objects defined primarily by their identity (`id`), rather than their attributes. They hold mutable state and define business invariants. We use Lombok's `@Builder` and restrict constructor access to enforce the use of explicit `create()` factory methods, preventing the creation of invalid states.

## Classes

### `User`
- **Responsibility**: The central identity representation for authentication and system access.
- **Key Attributes**: `identificationDocument`, `fullName`, `email`, `role`, `active`.
- **Domain Logic**: Starts strictly as `active = true` via the `create()` method. Exposes `suspend()` and `activate()` behaviors to control access.

### `BuyerProfile`
- **Responsibility**: Manages the specific commercial data of a buyer, separate from core authentication logic.
- **Key Attributes**: `userId` (link to User), `primaryAddress`, `secondaryAddresses`, `commercialStateActive`.
- **Domain Logic**: Can add multiple secondary addresses but enforces the existence of at least one primary address during creation.

### `SellerProfile`
- **Responsibility**: Represents a merchant's storefront in the marketplace.
- **Key Attributes**: `userId`, `storeName`, `taxId`, `contactEmail`, `active`.

### `Product`
- **Responsibility**: Represents a catalog item.
- **Key Attributes**: `sku`, `price` (Money), `type` (ProductType), `variants` (e.g., Color, Size).
- **Domain Logic**: Ensures that products are safely instantiated with all required metadata (name, price, sellerId) before being active in the catalog.

### `Warehouse`
- **Responsibility**: Represents a geographical storage location.
- **Key Attributes**: `ownerId`, `name`, `locationAddress`, `type` (WarehouseType).

### `InventoryItem`
- **Responsibility**: Manages physical and reserved stock for a specific SKU inside a specific Warehouse.
- **Key Attributes**: `physicalQuantity`, `reservedQuantity`, `damagedQuantity`.
- **Domain Logic**:
  - `getAvailableQuantity()`: Calculates actual availability (`physicalQuantity - reservedQuantity - damagedQuantity`).
  - `reserve()`: Secures items for checkout, preventing negative stock.
  - `reportDamaged()`: Isolates broken/damaged units so they cannot be sold or reserved (Critical Business Rule from functional specification).

### `OrderItem`
- **Responsibility**: A single line item within a customer's order.
- **Domain Logic**: Calculates its own `subTotal` dynamically safely using the `Money` value object.

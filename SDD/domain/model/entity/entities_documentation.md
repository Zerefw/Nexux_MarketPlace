# Entities Documentation

## Overview
The `entity` package contains domain objects whose identity is defined by a unique identifier (ID) rather than their attributes. These objects maintain mutable state and safeguard business invariants. Access to constructors is restricted to enforce the use of static factory methods (e.g., `create()`), preventing the instantiation of entities with invalid states.

## Implemented Classes

### `User`
- **Responsibility**: Central identity representation for authentication and access.
- **Key Attributes**: `identificationDocument`, `fullName`, `email`, `role`, `active`.
- **Domain Logic**: Strictly initializes in an active state. Exposes behaviors such as `suspend()` and `activate()` to control access according to administrative requirements.

### `BuyerProfile`
- **Responsibility**: Manages specific commercial data of a buyer, isolating this from basic authentication logic.
- **Key Attributes**: `userId` (link to `User`), `primaryAddress`, `secondaryAddresses`, `commercialStateActive`.
- **Domain Logic**: Facilitates the addition of secondary addresses, validating the integrity of shipping data.

### `SellerProfile`
- **Responsibility**: Represents the commercial profile of a seller (merchant) within the marketplace.
- **Key Attributes**: `userId`, `storeName`, `taxId`, `contactEmail`, `active`.
- **Domain Logic**: Maintains updated fiscal identifiers and coordinates store operability.

### `Product`
- **Responsibility**: Represents a base item within the commercial catalog.
- **Key Attributes**: `sku`, `price`, `type`, `variants`.
- **Domain Logic**: Guarantees that the item possesses mandatory meta-information and allows for logical deactivation from the catalog (`DeactivateProductService`).

### `Warehouse`
- **Responsibility**: Represents a geographic or operational storage location.
- **Key Attributes**: `ownerId`, `name`, `locationAddress`, `type`, `active`.
- **Domain Logic**: Allows registration and deactivation of operational logistical facilities.

### `InventoryItem`
- **Responsibility**: Administers physical stock and holdbacks for a specific SKU within a given warehouse.
- **Key Attributes**: `physicalQuantity`, `reservedQuantity`, `damagedQuantity`.
- **Domain Logic**:
  - `getAvailableQuantity()`: Calculates actual availability excluding shrinkage and holdbacks.
  - `reserve()`: Secures stock for the purchase process, protecting the system from negative balances.
  - `reportDamaged()`: Isolates damaged merchandise, preventing its commercialization (critical business rule).

### `OrderItem`
- **Responsibility**: Represents an individual purchase line within a global order.
- **Domain Logic**: Computes its own subtotal dynamically using value objects to guarantee arithmetic precision.

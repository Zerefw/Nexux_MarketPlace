# Domain Services Documentation

## Overview
The `service` package contains Domain Services. In Domain-Driven Design (DDD), Domain Services are stateless classes that encapsulate complex business logic which doesn't naturally fit inside a single Entity or Aggregate. They typically orchestrate operations spanning multiple distinct domain objects (e.g., interacting with both `Order` and `InventoryItem`).

## Classes

### `InventoryDomainService`
- **Responsibility**: Handles the complex logic of reserving physical inventory across potentially multiple inventory batches/warehouses when a customer places an order.
- **Flow**:
  1. Validates that the requested order and inventory item lists are present.
  2. Iterates over each `OrderItem` inside the `Order`.
  3. Calculates the `totalAvailable` stock across all matching `InventoryItem` entities.
  4. Throws a `DomainException` if stock is insufficient, preventing the transaction from proceeding.
  5. Distributes the reservation logic securely across the available units, modifying the internal `reservedQuantity` of the affected `InventoryItem` entities incrementally.

### `OrderFulfillmentDomainService`
- **Responsibility**: Manages the critical transition when an order's payment is confirmed.
- **Flow**:
  1. Modifies the `Order` aggregate state by invoking `markAsPaid()`.
  2. Iterates over the previously reserved `InventoryItem` entities that correspond to this order.
  3. Consumes the reserved stock permanently (converting a temporary `reservedQuantity` hold into an actual deduction of `physicalQuantity`).
  4. Ensures absolute consistency between the financial state of the application (PAID order) and the logistical state (Inventory consumed).

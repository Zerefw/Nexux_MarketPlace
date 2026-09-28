# Domain Services Documentation

## Overview
The `service` package contains Domain Services. According to Domain-Driven Design (DDD) principles, Domain Services are stateless classes that encapsulate complex business logic that does not inherently belong to a single Entity or Aggregate. Typically, they orchestrate operations spanning multiple distinct domain objects, guaranteeing transactional consistency and the integrity of business rules at a fundamental level.

## Inventory Module

### `InventoryReservationDomainService`
- **Responsibility**: Manages the critical and complex logic of reserving distributed physical inventory across multiple lots or warehouses at the moment a customer confirms the intent to purchase.
- **Execution Flow**:
  1. Validates the integrity of the lists corresponding to the requested order and the inventory items.
  2. Iterates sequentially over each `OrderItem` contained in the `Order`.
  3. Calculates the consolidated available stock across matching `InventoryItem` entities.
  4. Throws a domain exception if stock is insufficient, aborting the transaction and preventing inconsistencies.
  5. Securely distributes the reservation across available units, incrementing the internal `reservedQuantity` variable of affected entities.

### `InventoryReleaseDomainService`
- **Responsibility**: Restores the availability of previously withheld inventory, typically invoked when an order expires due to lack of payment or is explicitly cancelled.
- **Execution Flow**:
  1. Identifies active reservations linked to the aborted transaction.
  2. Atomically decrements the `reservedQuantity` variable in the corresponding `InventoryItem` objects.
  3. Ensures that the stock is relisted as available for future commercial transactions.

## Orders and Cart Module

### `OrderFulfillmentDomainService`
- **Responsibility**: Administers the critical state transition when the receipt of economic funds for an order is confirmed.
- **Execution Flow**:
  1. Mutates the state of the aggregated `Order` entity by executing the corresponding payment method.
  2. Iterates over the `InventoryItem` entities previously reserved for this specific order.
  3. Effects the definitive consumption of stock, converting the temporary retention (`reservedQuantity`) into a permanent deduction of the total physical quantity (`physicalQuantity`).
  4. Guarantees absolute consistency between the financial state of the application and the reality of the logistics system.

## Refunds and Returns Module

### `ReturnInventoryDomainService`
- **Responsibility**: Handles the reinstatement of units returned by buyers back into available physical stock within the logistical inventory.
- **Execution Flow**:
  1. Receives the post-delivery merchandise return instruction.
  2. Increments the physical stock of the `InventoryItem`s in the destination warehouse designated for returns.
  3. Maintains the traceability of returned items and guarantees that the stock increase aligns with refund resolutions processed by the application services.

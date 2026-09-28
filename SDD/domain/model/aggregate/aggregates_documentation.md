# Aggregates Documentation

## Overview
The `aggregate` package contains the Aggregate Roots. An Aggregate Root is a fundamental concept in Domain-Driven Design (DDD) that acts as a gateway and transactional boundary for a cluster of associated entities and value objects. External objects are only permitted to hold references to the Aggregate Root, thereby guaranteeing internal data consistency and compliance with business rules.

## Implemented Classes

### `Order`
- **Responsibility**: Manages the entire lifecycle of a commercial transaction. It functions as the strict Aggregate Root for the dependent `OrderItem` entities. It supports the Orders, Logistics, and Refunds modules.
- **State Management**: Rigorously enforces the state machine defined for orders:
  1. Starts in a pre-transactional state (`CART`) via the `CreateCartService`.
  2. Transitions to pending payment (`PENDING_PAYMENT`) via the `CheckoutOrderService`.
  3. Transitions to financial confirmation (`PAID`) via the `MarkOrderAsPaidService`.
  4. Transitions to the logistical stage (`SHIPPED`) via the `ShipOrderService`.
  5. Concludes the cycle (`DELIVERED`) via the `DeliverOrderService`.
  6. Can be cancelled (`CANCELLED`) via the `CancelOrderService`.
- **Guaranteed Invariants**:
  - **Encapsulation**: The internal collection of items is immutable to the outside, preventing modifications that bypass the aggregate's control methods.
  - **Item Administration**: The addition or subtraction of commercial references only proceeds when the aggregate is in the `CART` state.
  - **Financial Recalculation**: Any modification to the item list triggers an atomic recalculation of the order's total value.
  - **Confirmation Constraints**: It is imperative to have a valid delivery address and an order balance greater than zero to proceed to the payment stages.

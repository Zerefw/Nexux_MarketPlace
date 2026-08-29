# Aggregates Documentation

## Overview
The `aggregate` package contains Aggregate Roots (AR). An AR is a fundamental concept in Domain-Driven Design (DDD) that acts as a gateway and transaction boundary for a cluster of associated entities and value objects. External objects are only allowed to hold references to the Aggregate Root (typically via its ID), ensuring data consistency.

## Classes

### `Order`
- **Responsibility**: Manages the entire lifecycle of a customer purchase. It acts as the strict Aggregate Root for `OrderItem` entities.
- **State Management**: It strictly enforces the state machine defined in `OrderStatus`:
  1. Starts as `CART` via the `createCart()` factory method.
  2. Transitions to `PENDING_PAYMENT` via `checkout()`.
  3. Transitions to `PAID` via `markAsPaid()`.
  4. Moves to `SHIPPED` via `markAsShipped()`.
  5. Concludes at `DELIVERED_FINALIZED` via `markAsDelivered()`.
- **Invariants Enforced**:
  - **Encapsulation**: The list of items (`getItems()`) is returned as an unmodifiable list to prevent external modification without going through the aggregate's methods.
  - **Item Management**: Items can only be added (`addItem()`) while the order is strictly in the `CART` state.
  - **Financial Recalculation**: Every time an item is added, the aggregate automatically recalculates its internal `totalAmount` leveraging the `OrderItem` subtotals.
  - **Checkout Constraints**: An order cannot proceed to checkout without a configured `shippingAddress` and at least one item inside the cart.

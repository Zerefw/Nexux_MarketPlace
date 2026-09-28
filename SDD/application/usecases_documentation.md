# Application Services Documentation (Use Cases)

## Overview
The `usecase` package acts as the system orchestrator. It implements the inbound ports defined in the Hexagonal Architecture and coordinates domain entities with the outbound ports (Adapters). The Use Cases contain the application-specific business logic, delegating pure domain logic to the entities. It strictly adheres to the Single Responsibility Principle.

## Users and Profiles Module

### `RegisterBuyerService`
- **Purpose**: Manages the initial registration of new buyers on the platform.
- **Transactionality**: Ensures the atomic creation of the user credential and the buyer profile (`BuyerProfile`).
- **Logic**: Verifies the prior non-existence of the email and identity document before persisting data, delegating password encryption to the corresponding port.

### `RegisterSellerService`
- **Purpose**: Administers the registration process for sellers.
- **Transactionality**: Guarantees synchronization between basic user data and seller-specific metadata (`SellerProfile`).
- **Logic**: Validates credentials and structures corporate profiles before invoking the repository ports.

### `SuspendUserService`
- **Purpose**: Blocks access for a user due to infractions or administrative reasons.
- **Logic**: Transitions the account state to inactive and revokes active session tokens or restricts future operations.

### `ActivateUserService`
- **Purpose**: Reactivates previously suspended user accounts or those pending verification.
- **Logic**: Restores operational privileges within the platform.

### `AddSecondaryAddressService`
- **Purpose**: Allows a buyer to append multiple shipping addresses.
- **Logic**: Associates a new `Address` with the buyer's aggregate, validating the integrity of the destination address format.

### `UpdateSellerTaxIdService`
- **Purpose**: Updates the fiscal information (tax identification) of a seller.
- **Logic**: Performs uniqueness checks on fiscal identifiers in the system before persisting modifications in the seller's profile.

## Catalog Module

### `CreateProductService`
- **Purpose**: Instantiates a base product within the commercial catalog.
- **Transactionality**: Employs a transactional context to prevent partial metadata writing.
- **Logic**: Prevents SKU code duplication and delegates instantiation to the factory method `Product.create()`.

### `UpdateProductService`
- **Purpose**: Modifies attributes of an existing product, such as descriptions or base rates.
- **Logic**: Retrieves the original entity, applies mutations permitted by the domain model, and consolidates the changes.

### `DeactivateProductService`
- **Purpose**: Performs a logical deletion of a catalog item.
- **Logic**: Transitions the product state to exclude it from public queries without compromising the referential integrity of past orders.

### `AddProductVariantService`
- **Purpose**: Manages the creation of specific variants for a base product (e.g., dimensions, colors).
- **Logic**: Validates the product's existence and adds child entities of type `ProductVariant`, ensuring no SKU collisions occur for variants.

### `FindProductBySkuService`
- **Purpose**: Queries comprehensive details of a product using its unique identifier (SKU).
- **Logic**: Coordinates data extraction from read repositories to project the information to the presentation layer.

### `ListActiveProductsService`
- **Purpose**: Returns the catalog of products available for sale.
- **Logic**: Implements filters and pagination over the repositories, exclusively returning instances whose status is active.

## Warehouses Module

### `RegisterWarehouseService`
- **Purpose**: Registers the opening of a new physical or virtual warehouse in the logistics network.
- **Logic**: Stores coordinates and operational metadata of the storage facilities.

### `DeactivateWarehouseService`
- **Purpose**: Closes the commercial operations of a warehouse.
- **Logic**: Prevents the routing of new receipts and dispatches to the disabled logistical node.

## Inventory Module

### `AddPhysicalStockService`
- **Purpose**: Processes the formal entry of new merchandise into physical facilities.
- **Logic**: Increases the physical stock of a specific variant in a given warehouse.

### `ReportDamagedStockService`
- **Purpose**: Manages the exclusion of merchandise from the commercial inventory due to shrinkage or damage.
- **Logic**: Deducts the affected units from the available stock balance and logs the event for auditing.

### `GetAvailableStockService`
- **Purpose**: Consolidates and exposes inventory availability for store consumption.
- **Logic**: Calculates the net balance by subtracting active reservations from the total physical stock.

## Orders and Cart Module

### `CreateCartService`
- **Purpose**: Initializes an order structure in a pre-transactional state (CART).
- **Logic**: Creates the record linking the buyer to the context of the new e-commerce transaction.

### `AddItemToCartService`
- **Purpose**: Adds commercial references to the shopping cart.
- **Logic**: Validates operational limits and invokes domain logic to recalculate the financial totals of the order.

### `RemoveItemFromCartService`
- **Purpose**: Removes previously selected items from the shopping cart.
- **Logic**: Updates the order structure and atomically recalculates the corresponding balances.

### `CheckoutOrderService`
- **Purpose**: Executes the transition from the CART to the PENDING_PAYMENT state.
- **Transactionality**: Ensures that inventory freezing (via domain services) coincides with the order state update.
- **Logic**: Coordinates inventory services to reserve merchandise before issuing the payment intention.

### `CancelOrderService`
- **Purpose**: Cancels an order that has not passed the payment phase.
- **Logic**: Coordinates the release of inventory reservations and mutates the order state to CANCELLED.

## Logistics and Payments Module

### `MarkOrderAsPaidService`
- **Purpose**: Registers the confirmation of the financial settlement of the order.
- **Logic**: Invokes fulfillment domain services to transform reservations into definitive physical deductions and marks the order as PAID.

### `ShipOrderService`
- **Purpose**: Notifies the physical dispatch of the order.
- **Logic**: Updates the logistical trace by changing the operational state to SHIPPED.

### `DeliverOrderService`
- **Purpose**: Confirms the receipt of the merchandise by the final buyer.
- **Logic**: Closes the operational cycle of the order by setting the final state to DELIVERED.

## Refunds and Returns Module

### `ProcessRefundService`
- **Purpose**: Initiates the financial and logistical return process for delivered orders.
- **Logic**: Validates return policies, issues the financial refund instruction, and coordinates the logistical return with relevant domain services.

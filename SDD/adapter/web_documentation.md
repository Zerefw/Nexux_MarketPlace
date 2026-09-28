# Infrastructure Layer Documentation (Web Adapters)

## Overview
The `adapter.in.web` package contains the Primary (inbound) Adapters. These REST controllers are responsible for receiving HTTP requests from clients, mapping incoming data structures to standard Use Case input commands, and returning a unified and consistent JSON response.

## Implementation Standards
- **Global Responses**: All endpoints return an object of type `ApiResponse<T>` to maintain a strict contract with the API consumers (`{ success: true, message: "...", data: {...} }`).
- **Data Transfer Objects (DTO)**: Domain entities are never directly exposed through the controllers. Information is mapped using DTOs (e.g., `UserResponseDTO`, `ProductResponseDTO`) to hide internal logic and sensitive data.

## Projected Controllers (30 Services)

### Authentication and User Controller (`UserController` / `AuthController`)
- Manages the services: `RegisterBuyerService`, `RegisterSellerService`, `SuspendUserService`, `ActivateUserService`, `AddSecondaryAddressService`, `UpdateSellerTaxIdService`.

### Catalog Controller (`ProductCatalogController`)
- Manages the services: `CreateProductService`, `UpdateProductService`, `DeactivateProductService`, `AddProductVariantService`, `FindProductBySkuService`, `ListActiveProductsService`.

### Warehouse Controller (`WarehouseController`)
- Manages the services: `RegisterWarehouseService`, `DeactivateWarehouseService`.

### Inventory Controller (`InventoryController`)
- Manages the services: `AddPhysicalStockService`, `ReportDamagedStockService`, `GetAvailableStockService`.

### Order and Cart Controller (`OrderController`)
- Manages the services: `CreateCartService`, `AddItemToCartService`, `RemoveItemFromCartService`, `CheckoutOrderService`, `CancelOrderService`.

### Logistics Controller (`LogisticsController`)
- Manages the services: `MarkOrderAsPaidService`, `ShipOrderService`, `DeliverOrderService`.

### Refund Controller (`RefundController`)
- Manages the service: `ProcessRefundService`.

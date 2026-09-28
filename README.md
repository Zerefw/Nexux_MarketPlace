# NexusMarket: Centralized E-commerce Platform

Welcome to the main repository for NexusMarket. This platform acts as an advanced commercial intermediary between buyers and sellers, comprehensively managing the full lifecycle of an e-commerce operation: catalog, multi-warehouse configuration, distributed inventory, shopping carts, billing, and logistics.

---

## 1. Architecture and Technologies

The project is designed to be highly scalable and maintainable using Domain-Driven Design (DDD) and Hexagonal Architecture (Ports and Adapters). This ensures that pure business logic is completely isolated from databases, frameworks, or user interfaces.

### Technology Stack:
- **Language:** Java 17
- **Main Framework:** Spring Boot 4.1.0 (or 3.x)
- **Hybrid Persistence:** Spring Data JPA (MySQL) for ACID transactions and Spring Data MongoDB for flexible models or high volume (e.g., Audit or logs).
- **Core Tools:** Lombok (boilerplate code reduction), Spring Security (Authentication and JWT).

---

## 2. Project Structure (Domain, Application, and Adapters)

Currently, the project has fully completed the Domain, Application, and Infrastructure (Adapters) for the User Registration and Product Catalog milestones.

```text
src/main/java/application/
├── domain/                           # Core business logic (100% Complete)
│   ├── exception/, model/, service/  
├── port/                             # Hexagonal Ports (Interfaces)
│   ├── in/                           # Inbound Ports
│   └── out/                          # Outbound Ports
├── usecase/                          # Application Services
│   ├── RegisterUserService.java      
│   └── ProductCatalogService.java    # Catalog management logic
├── adapter/                          # Infrastructure Layer
│   ├── in/web/                       # REST Controllers and DTOs
│   │   ├── AuthController.java
│   │   ├── ProductController.java    # Access points for the catalog
│   │   └── dto/ApiResponse.java
│   └── out/persistence/              # JPA Repositories and Database Entities
│       ├── entity/                   # UserJpaEntity, ProductJpaEntity...
│       ├── repository/               # JpaRepositories
│       ├── mapper/                   # Mappers (Domain <-> JPA)
│       └── adapter/                  # Persistence Adapters
└── config/                           # Framework and security configuration
    └── SecurityConfig.java
```

> **Note:** Each architectural layer has detailed documentation in the root directory `SDD/` (Software Design Document).

---

## 3. Business Operational Flow

The design strictly adheres to the Functional Specification, ensuring that state changes and inventory reservations occur under secure rules.

```mermaid
flowchart TD
    subgraph OrderLifecycle [Order Lifecycle]
        A([CART]) -->|checkout| B([PENDING_PAYMENT])
        B -->|validated payment| C([PAID])
        C -->|physical dispatch| D([SHIPPED])
        D -->|confirmed delivery| E([DELIVERED_FINALIZED])
    end

    subgraph InventoryImpact [Inventory Impact]
        B -.->|temporary reservation| R[Increase reserved quantity]
        C -.->|permanent consumption| C2[Decrease physical and reserved quantity]
    end
```

### Critical Domain Validations Implemented:
1. **Damaged Stock Management**: Units marked as damaged (`damagedQuantity`) in `InventoryItem` cannot be reserved or added to available stock.
2. **Atomic Reservation**: The `InventoryDomainService` securely manages stock reservations across multiple warehouses simultaneously.
3. **Order Consistency**: An order in `CART` status cannot proceed to checkout without a registered shipping address.
4. **Digital vs. Physical Products**: Clear distinction (`ProductType`) strictly mapped to database constraints.

---

## 4. Implementation Steps and Milestones

- [x] **Milestone 1: Base Configuration** (COMPLETED)
- [x] **Milestone 2: Core Domain Modeling** (COMPLETED)
- [x] **Milestone 3: Authentication, Ports, and Adapters** (COMPLETED)
- [x] **Milestone 4: Catalog and Distributed Inventory Module (COMPLETED)**:
  - [x] Product Application Services (`ProductCatalogService`).
  - [x] Persistence adapters, mappers, and JPA entities (`ProductJpaEntity`).
  - [x] REST Web Controllers (`ProductController`).
- [ ] **Milestone 5: Cart Engine and Concurrent Orders**.
- [ ] **Milestone 6: Billing and Post-Sales Logistics**.

### Extended Execution Phases (Based on Granular 30-Service Final Plan)
- [x] **Phase 1 (Documentation and README):** Update all .md files in the SDD/ folder and the main README.md to reflect this new granular 30-service design.
- [ ] **Phase 2 (Complete Domain):** Ensure that the necessary Entities, Value Objects, and Exceptions for these 30 services exist. Comment existing code in Spanish.
- [ ] **Phase 3 (Ports and Domain Services):** Create all missing interfaces (Inbound/Outbound Ports) and Domain Services.
- [ ] **Phase 4 (Application Services Part 1):** Implement services 1 to 14.
- [ ] **Phase 5 (Application Services Part 2):** Implement services 15 to 30.

---
*Document generated and maintained for the architectural traceability of NexusMarket.*

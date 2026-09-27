# Infrastructure Layer (Persistence Adapters) Documentation

## Overview
The `adapter.out.persistence` package represents the **Secondary Adapters** (Outbound). This layer implements the Output Ports defined by the Application Layer. Its sole responsibility is to translate Domain requests into Database queries (MySQL) using Spring Data JPA.

## Architecture Constraints
- **Mappers (`ProductMapper`, `UserMapper`, `ProfileMapper`)**: Strict boundary enforcers. They translate pure Domain Entities into `@Entity` JPA classes (and vice versa). The Domain Layer *never* sees a JPA entity.
- **JPA Entities (`*JpaEntity`)**: Contain Spring/Hibernate annotations (`@Table`, `@Column`). Designed purely to reflect the relational database schema.

## Implemented Adapters

### `UserPersistenceAdapter`
- Implements `UserRepositoryPort`.
- Uses `UserJpaRepository`.
- **Security integration**: Handles the transition of the `encodedPassword` from the application layer into the database structure.

### `ProfilePersistenceAdapter`
- Implements both `BuyerProfileRepositoryPort` and `SellerProfileRepositoryPort`.
- Uses their respective JpaRepositories to store addresses, tax IDs, and commercial statuses.

### `ProductPersistenceAdapter`
- Implements `ProductRepositoryPort`.
- Uses `ProductJpaRepository`.
- **Complexity**: The `ProductMapper` handles the conversion of nested Domain Value Objects (`Money`, `Sku`) into flattened JPA columns (`priceAmount`, `priceCurrency`, `sku`), and handles Lists (like `variants`) by joining them into a `TEXT` database column.

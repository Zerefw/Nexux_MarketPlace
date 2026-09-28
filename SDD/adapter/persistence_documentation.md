# Infrastructure Layer Documentation (Persistence Adapters)

## Overview
The `adapter.out.persistence` package represents the Secondary (outbound) Adapters. This layer implements the outbound ports defined by the application layer. Its sole responsibility is to translate domain requests into database queries (MySQL) using Spring Data JPA. It supports the persistence operations required by the 30 services defined in the system.

## Architectural Constraints
- **Mappers (`ProductMapper`, `UserMapper`, `ProfileMapper`)**: Strict enforcers of architectural boundaries. They translate pure domain entities into `@Entity` JPA classes and vice versa. The domain layer never interacts directly with a JPA entity.
- **JPA Entities (`*JpaEntity`)**: Contain Spring and Hibernate annotations (`@Table`, `@Column`). They are designed exclusively to reflect the relational database schema.

## Implemented Adapters

### `UserPersistenceAdapter`
- Implements the `UserRepositoryPort` interface.
- Uses the `UserJpaRepository` interface.
- **Security Integration**: Handles the transition of the encrypted password from the application layer to the persistence structure in the database.

### `ProfilePersistenceAdapter`
- Simultaneously implements the `BuyerProfileRepositoryPort` and `SellerProfileRepositoryPort` interfaces.
- Uses their respective JPA repositories to store addresses, tax identifiers, and commercial statuses.

### `ProductPersistenceAdapter`
- Implements the `ProductRepositoryPort` interface.
- Uses the `ProductJpaRepository` interface.
- **Complexity**: The `ProductMapper` manages the conversion of nested domain value objects (`Money`, `Sku`) into flattened JPA columns (`priceAmount`, `priceCurrency`, `sku`), and manages list structures (such as product variants) by serializing them into text-type database columns.

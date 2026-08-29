# 🛒 NexusMarket: Plataforma de Comercio Electrónico Centralizada

Bienvenido al repositorio principal de **NexusMarket**. Esta plataforma actúa como un intermediario comercial avanzado entre compradores y vendedores, gestionando de forma integral el ciclo de vida completo de un e-commerce: catálogo, multi-bodega, inventario distribuido, carritos de compra, facturación y logística.

---

## 🏗️ 1. Arquitectura y Tecnologías

El proyecto está diseñado para ser altamente escalable y mantenible utilizando **Domain-Driven Design (DDD)** y **Arquitectura Hexagonal (Puertos y Adaptadores)**. Esto garantiza que la lógica de negocio pura esté completamente aislada de bases de datos, frameworks o interfaces de usuario.

### Stack Tecnológico:
- **Lenguaje:** Java 17
- **Framework Principal:** Spring Boot 4.1.0 (o 3.x)
- **Persistencia Híbrida:** Spring Data JPA (MySQL) para transacciones ACID y Spring Data MongoDB para modelos flexibles o alto volumen (ej. Auditoría o logs).
- **Herramientas Core:** Lombok (reducción de boilerplate), Spring Security (Auth & JWT).

---

## 📂 2. Estructura del Proyecto (Capa de Dominio)

Actualmente, el proyecto tiene implementado al 100% su **Core Domain** bajo el paquete base `application.domain`.

```text
src/main/java/application/
└── domain/
    ├── exception/                    # 🚨 Excepciones de negocio aisladas
    │   └── DomainException.java      
    ├── model/
    │   ├── aggregate/                # 📦 Aggregate Roots (Raíces de Agregación)
    │   │   └── Order.java            # Controla el ciclo de vida del carrito y pedido
    │   ├── entity/                   # 🧍 Entidades con Identidad propia
    │   │   ├── User.java             # Identidad central de autenticación
    │   │   ├── BuyerProfile.java     # Gestión del comprador (direcciones)
    │   │   ├── SellerProfile.java    # Gestión del vendedor (tienda)
    │   │   ├── Product.java          # Catálogo (físico/digital, variantes)
    │   │   ├── Warehouse.java        # Bodegas del Marketplace o Vendedor
    │   │   ├── InventoryItem.java    # Control de stock, reservas y dañados
    │   │   └── OrderItem.java        # Ítems individuales dentro de un Order
    │   └── valueobject/              # 🏷️ Value Objects Inmutables y Enums
    │       ├── Money.java            # Cálculos seguros de dinero y moneda
    │       ├── Sku.java, Email.java, StockLocation.java
    │       ├── UserRole.java         # BUYER, SELLER, LOGISTICS_OPERATOR, ADMIN, SUPERVISOR
    │       ├── OrderStatus.java      # Estados estrictos del pedido
    │       ├── ProductType.java      # PHYSICAL vs DIGITAL
    │       └── WarehouseType.java    
    └── service/                      # ⚙️ Domain Services (Lógica Orquestada)
        ├── InventoryDomainService.java
        └── OrderFulfillmentDomainService.java
```

> **Nota:** Cada carpeta del dominio cuenta con su documentación detallada y en inglés en la carpeta raíz `SDD/` (Software Design Document).

---

## 🔄 3. Flujo Operativo del Negocio

El diseño respeta estrictamente la **Especificación Funcional**, asegurando que los cambios de estado y las reservas de inventario ocurran bajo reglas seguras.

```mermaid
flowchart TD
    subgraph Ciclo de Vida del Pedido (Order)
        A([CART]) -->|checkout() + Dirección| B([PENDING_PAYMENT])
        B -->|Pago Validado| C([PAID])
        C -->|Despacho Físico| D([SHIPPED])
        D -->|Entrega Confirmada| E([DELIVERED_FINALIZED])
    end

    subgraph Impacto en Inventario (InventoryItem)
        B -.->|Reserva Temporal| R[Aumenta reservedQuantity]
        C -.->|Consumo Definitivo| C2[Disminuye physicalQuantity y reservedQuantity]
    end
```

### Validaciones Críticas del Dominio Implementadas:
1. **Gestión de Stock Dañado**: Unidades marcadas como dañadas (`damagedQuantity`) en `InventoryItem` jamás pueden ser reservadas ni sumadas al stock disponible (`getAvailableQuantity()`).
2. **Reserva Atómica**: El `InventoryDomainService` permite asegurar el stock de múltiples bodegas simultáneamente. Si falta aunque sea un ítem de un SKU, la operación entera es rechazada.
3. **Consistencia de Pedido**: No se puede proceder al pago (`PENDING_PAYMENT`) de un pedido en `CART` si no se ha registrado una dirección de envío o si el carrito está vacío.
4. **Productos Digitales vs Físicos**: Distinción clara en la base (`ProductType`) para poder adaptar la logística tradicional.

---

## 🚀 4. Próximos Pasos de Implementación

Hemos culminado la **Fase 1 (Setup)** y **Fase 2 (Modelado de Dominio)**. Los siguientes hitos a desarrollar en este repositorio son:

- [ ] **Hito 3: Autenticación, Puertos y Adaptadores**:
  - Creación de los Casos de Uso (Application Services) en `application.usecase`.
  - Definición de interfaces (`InputPort`, `OutputPort`).
  - Implementación de controladores REST (`adapter.in.web`) para conectar Frontends.
  - Implementación de Repositorios MongoDB/MySQL (`adapter.out.persistence`).
  - Configuración del Filtro JWT y roles de Spring Security.
- [ ] **Hito 4: Módulo de Catálogo e Inventario Distribuido**.
- [ ] **Hito 5: Motor de Carrito y Órdenes Concurrentes**.
- [ ] **Hito 6: Facturación y Logística Post-Venta**.

---
*Documento generado y mantenido para la trazabilidad arquitectónica de NexusMarket.*

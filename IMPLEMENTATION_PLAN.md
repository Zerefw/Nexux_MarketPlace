# Plan de Implementación: Nexus MarketPlace (DDD + Hexagonal Architecture)

Este documento detalla el plan paso a paso para construir la API del Nexus MarketPlace. Está dividido en fases progresivas, diseñadas para generar **entregables tangibles** (especialmente orientados a la revisión académica de Modelos y Servicios de Dominio) antes de avanzar a la infraestructura y los endpoints.

## 📌 Hito 1: Setup y Configuración Base
Configuración inicial del proyecto Spring Boot y estructura de paquetes de la Arquitectura Hexagonal.

- [ ] **1.1. Inicialización del Proyecto Spring Boot**
  - [ ] Crear el proyecto base (`pom.xml` o Gradle) con Java 17+.
  - [ ] Agregar dependencias: Web, Data JPA, Security, Validation, MySQL Driver.
  - [ ] Agregar dependencias JWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) y OpenAPI (Swagger).
- [ ] **1.2. Estructura de Paquetes (Hexagonal)**
  - [ ] Crear carpetas: `domain`, `application`, `adapter`, `config`, `shared`.
  - [ ] Dentro de `domain`: `model` (aggregate, entity, valueobject), `service`, `exception`.
  - [ ] Dentro de `application`: `port` (input, output), `usecase`, `dto`.
  - [ ] Dentro de `adapter`: `in` (web, scheduler), `out` (persistence).
- [ ] **1.3. Configuraciones Transversales**
  - [ ] Crear `GlobalExceptionHandler` con `ApiResponse<T>`.
  - [ ] Configurar `application.properties` para MySQL local/H2.

---

## 📌 Hito 2: Diseño del Core Domain (ENTREGABLE ACADÉMICO PRINCIPAL)
Este entregable contiene la lógica de negocio pura, sin dependencias de Spring ni JPA. Es fundamental para demostrar el uso de DDD.

- [ ] **2.1. Value Objects & Enums**
  - [ ] Crear VOs comunes: `Money`, `Email`, `PhoneNumber`.
  - [ ] Crear VOs específicos: `Sku`, `StockLocation`.
  - [ ] Crear Enums: `UserRole`, `UserStatus`, `ProductStatus`, `OrderStatus`, `TransferStatus`.
- [ ] **2.2. Entidades de Dominio y Agregados (Aggregates)**
  - [ ] **Agregado User:** Atributos básicos, rol, estado, y fábrica estática `create(...)`.
  - [ ] **Agregado Store:** Perfil del vendedor.
  - [ ] **Agregado Product:** Atributos de catálogo, variantes.
  - [ ] **Agregado Warehouse & InventoryItem:** Lógica de stock (físico, reservado, disponible).
  - [ ] **Agregado Order:** Ítems de la orden, estado, cálculos de totales.
  - [ ] **Entidad AuditLog:** Registro inmutable de eventos.
- [ ] **2.3. Excepciones de Dominio**
  - [ ] `DomainValidationException`, `InsufficientStockException`, `OrderStateException`, `ResourceNotFoundException`.
- [ ] **2.4. Servicios de Dominio (Domain Services)**
  - [ ] `InventoryReservationDomainService`: Lógica atómica para descontar stock al crear una orden.
  - [ ] `OrderFulfillmentDomainService`: Lógica de validación de pagos y transición a despacho.

---

## 📌 Hito 3: Autenticación, Seguridad y Gestión de Usuarios
Implementación del flujo de login, JWT y persistencia de usuarios.

- [ ] **3.1. Puertos de Aplicación (Ports)**
  - [ ] `UserInputPort` (Input) y `UserRepositoryPort` (Output).
- [ ] **3.2. Casos de Uso (Application Services)**
  - [ ] Implementar `UserUseCase` (Login, Registro, Consulta).
- [ ] **3.3. Adaptadores de Persistencia (Driven Adapters)**
  - [ ] Crear `UserJpaEntity` (Anotaciones Hibernate/JPA).
  - [ ] Crear `UserJpaRepository` (Spring Data).
  - [ ] Crear `UserPersistenceMapper` (`toDomain`, `toJpaEntity`).
  - [ ] Crear `UserPersistenceAdapter` (Implementa `UserRepositoryPort`).
- [ ] **3.4. Seguridad y Configuración**
  - [ ] Implementar `JwtTokenProvider` y `JwtAuthenticationFilter`.
  - [ ] Configurar `SecurityConfig` (CORS, CSRF, Rutas públicas/privadas, Stateless session).
  - [ ] Crear `SecurityContextHelper` para extraer el usuario en sesión.
- [ ] **3.5. Controladores REST (Driving Adapters)**
  - [ ] Implementar `AuthController` (`/api/auth/login`, `/register`).
  - [ ] Implementar `UserController` (`/api/users`).

---

## 📌 Hito 4: Módulo de Catálogo e Inventario Distribuido
Implementación completa (Casos de uso + Persistencia + REST) para productos y bodegas.

- [ ] **4.1. Catálogo de Productos**
  - [ ] Puertos: `ProductInputPort`, `ProductRepositoryPort`.
  - [ ] Casos de uso: `ProductUseCase` (CRUD para `SELLER` y `ADMIN`, búsqueda para `BUYER`).
  - [ ] Persistencia: `ProductJpaEntity`, Mapper, Repositorio y Adaptador.
  - [ ] REST: `ProductController`.
- [ ] **4.2. Gestión de Inventario Multi-Bodega**
  - [ ] Puertos: `InventoryInputPort`, `InventoryRepositoryPort`, `WarehouseRepositoryPort`.
  - [ ] Casos de uso: `InventoryUseCase` (Entradas de stock, transferencias, ajustes).
  - [ ] Persistencia: `InventoryItemJpaEntity`, `WarehouseJpaEntity`, Mappers, Repositorios y Adaptadores.
  - [ ] REST: `InventoryController`.

---

## 📌 Hito 5: Carrito, Órdenes y Concurrencia
El núcleo transaccional del marketplace.

- [ ] **5.1. Flujo de Órdenes (Creación y Reserva)**
  - [ ] Puertos: `OrderInputPort`, `OrderRepositoryPort`.
  - [ ] Casos de uso: `OrderUseCase` (Crear orden, calcular totales).
  - [ ] Integración: Uso de `InventoryReservationDomainService` dentro de `OrderUseCase` para reservar stock.
  - [ ] Persistencia: `OrderJpaEntity`, Mappers, Repositorio y Adaptador.
  - [ ] REST: `OrderController`.
- [ ] **5.2. Scheduler de Expiración (Procesos en Background)**
  - [ ] Implementar `OrderExpiryScheduler` (Verifica cada N minutos órdenes en `PENDING_PAYMENT`).
  - [ ] Lógica para liberar stock reservado si la orden caduca.

---

## 📌 Hito 6: Facturación, Pagos y Logística
Cierre del flujo transaccional.

- [ ] **6.1. Simulación de Pagos y Facturación**
  - [ ] Casos de uso: `InvoiceUseCase` (Cambio de estado de orden a `PAID`, generación de desglose financiero y comisión).
  - [ ] Persistencia y REST para facturas.
- [ ] **6.2. Envíos y Logística (Shipments)**
  - [ ] Casos de uso: `ShipmentUseCase` (Asignación de guía, actualización de estados de envío).
  - [ ] Integración: Uso de `OrderFulfillmentDomainService` para marcar la orden como entregada y despachar inventario.
  - [ ] Persistencia y REST para envíos.

---

## 📌 Hito 7: Auditoría y Cierre
Trazabilidad de operaciones y ajustes finales.

- [ ] **7.1. Módulo de Auditoría Inmutable**
  - [ ] Interfaz de captura de logs (AOP o invocación directa en casos de uso críticos).
  - [ ] Persistencia: `AuditLogJpaEntity` (con campo `@Lob` JSON para `detail_data`).
  - [ ] REST: `AuditLogController` (Solo lectura para administradores).
- [ ] **7.2. Data Seeder (Población inicial de BD)**
  - [ ] Crear un `DataSeeder` (CommandLineRunner) para cargar Roles, Admins iniciales, 2 bodegas de prueba y productos básicos.
- [ ] **7.3. Pruebas y Refinamiento**
  - [ ] Pruebas unitarias de los Servicios de Dominio (Ej. validación estricta de que el stock no quede negativo).
  - [ ] Pruebas de integración de los repositorios JPA.

---

## 📝 Resumen para el Profesor (Entregables)

1. **Modelos de Dominio:** Todas las clases dentro de `domain/model/aggregate` y `domain/model/entity`. Mostrarán encapsulamiento, nula dependencia de frameworks (cero `@Entity` o `@Autowired`) y uso de métodos de fábrica (`create()`).
2. **Value Objects:** Clases en `domain/model/valueobject`, demostrando inmutabilidad y validaciones intrínsecas (ej. un `Money` que no permite monedas distintas en sumas).
3. **Servicios de Dominio:** Clases en `domain/service`, demostrando lógica compleja que afecta múltiples agregados (Ej: `InventoryReservationDomainService` validando si hay suficiente stock disponible antes de crear la orden).
4. **Arquitectura Limpia:** La estructura de carpetas evidenciará los puertos de entrada/salida y la inyección de dependencias en la capa de Aplicación.

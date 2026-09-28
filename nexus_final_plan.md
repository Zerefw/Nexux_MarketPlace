# Plan de Implementación Final - NexusMarket (>28 Servicios)

Este documento detalla la estrategia de implementación final para el proyecto NexusMarket, cumpliendo con los estándares de Arquitectura Hexagonal, Domain-Driven Design (DDD) y los principios SOLID (específicamente el Principio de Responsabilidad Única para los Casos de Uso, lo que justifica la existencia de más de 28 servicios).

## 1. Reglas de Codificación Obligatorias
- **Arquitectura:** Hexagonal Estricta (Dominio > Aplicación > Infraestructura).
- **SOLID:** Un Caso de Uso (Application Service) por cada acción específica del sistema.
- **Documentación:** Javadocs obligatorios en todos los métodos públicos, clases e interfaces. **ESTRICTAMENTE EN ESPAÑOL Y SIN EMOJIS**.

## 2. Inventario de Servicios a Implementar (Total: 30 Servicios)

### Módulo de Usuarios y Perfiles (Application Services)
1. `RegisterBuyerService`: Registra compradores.
2. `RegisterSellerService`: Registra vendedores.
3. `SuspendUserService`: Bloquea acceso a un usuario.
4. `ActivateUserService`: Reactiva a un usuario.
5. `AddSecondaryAddressService`: Añade direcciones a un comprador.
6. `UpdateSellerTaxIdService`: Actualiza datos fiscales del vendedor.

### Módulo de Catálogo (Application Services)
7. `CreateProductService`: Crea un producto base.
8. `UpdateProductService`: Modifica descripción o precio.
9. `DeactivateProductService`: Borrado lógico del producto.
10. `AddProductVariantService`: Añade variantes (tallas/colores).
11. `FindProductBySkuService`: Consulta detalle de un producto.
12. `ListActiveProductsService`: Retorna catálogo público.

### Módulo de Bodegas (Application Services)
13. `RegisterWarehouseService`: Registra una nueva bodega.
14. `DeactivateWarehouseService`: Cierra operaciones de una bodega.

### Módulo de Inventario (Application & Domain Services)
15. `AddPhysicalStockService`: Ingreso de mercancía física.
16. `ReportDamagedStockService`: Reporte de mermas.
17. `GetAvailableStockService`: Consulta consolidada de disponibilidad.
18. `InventoryReservationDomainService`: (Dominio) Reserva atómica distribuida.
19. `InventoryReleaseDomainService`: (Dominio) Liberación de reserva por caducidad.

### Módulo de Órdenes y Carrito (Application & Domain Services)
20. `CreateCartService`: Inicializa una orden en estado CART.
21. `AddItemToCartService`: Añade ítems recalculando totales.
22. `RemoveItemFromCartService`: Quita ítems del carrito.
23. `CheckoutOrderService`: Transiciona de CART a PENDING_PAYMENT.
24. `CancelOrderService`: Cancela orden no pagada.
25. `OrderFulfillmentDomainService`: (Dominio) Pasa orden a pagada y consume inventario físico.

### Módulo de Logística y Pagos (Application Services)
26. `MarkOrderAsPaidService`: Registra confirmación financiera.
27. `ShipOrderService`: Despacha la orden físicamente.
28. `DeliverOrderService`: Cierra el ciclo de entrega.

### Módulo de Reembolsos y Devoluciones (Application & Domain Services)
29. `ProcessRefundService`: Inicia proceso de devolución de dinero.
30. `ReturnInventoryDomainService`: (Dominio) Reintegra unidades devueltas al stock físico.

## 3. Fases de Ejecución (Subagentes Secuenciales)

- **Fase 1 (Documentación y README):** Actualizar todos los archivos `.md` en la carpeta `SDD/` y el `README.md` principal para reflejar este nuevo diseño granular de 30 servicios.
- **Fase 2 (Completar Dominio):** Asegurar que las entidades, Value Objects y Excepciones necesarias para estos 30 servicios existan. Comentar el código existente en español (sin emojis).
- **Fase 3 (Puertos y Servicios de Dominio):** Crear todas las interfaces (Input/Output Ports) y los Domain Services faltantes.
- **Fase 4 (Servicios de Aplicación Parte 1):** Implementar los servicios 1 al 14.
- **Fase 5 (Servicios de Aplicación Parte 2):** Implementar los servicios 15 al 30.

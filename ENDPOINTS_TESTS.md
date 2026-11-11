# Guía Definitiva de Pruebas en Postman - NexusMarket API

> **¿Por qué estabas perdido?**  
> Porque en un marketplace real hay una **cadena lógica de dependencias**: no puedes agregar un producto al carrito si el vendedor no lo ha creado, y no puedes hacer checkout si no hay stock físico en la bodega.  
>  
> Esta guía te da el **orden exacto (clic por clic)** para probar la colección importada en Postman (`NexusMarket API`) de inicio a fin sin errores y sin tener que copiar ni pegar IDs ni tokens manualmente.

---

## 1. Regla de Oro: Cómo funciona la magia de las Variables en Postman

Al importar `NexusMarket.postman_collection.json`, cada petición tiene un **script automático de test**.  
Cuando le das a **Send**, Postman guarda automáticamente en la memoria de la colección:
- Los tokens JWT: `{{buyerToken}}`, `{{sellerToken}}`, `{{adminToken}}`, `{{logisticsToken}}`.
- Los identificadores generados por la base de datos: `{{buyerId}}`, `{{sellerId}}`, `{{warehouseId}}`, `{{orderId}}` y `{{sku}}`.

**Tú solo tienes que abrir la carpeta correspondiente y darle a Send en el orden numérico que se indica abajo.**

---

## 2. Ruta Maestra de Ejecución Paso a Paso (El Camino Feliz)

Sigue esta secuencia numerada en tu Postman:

```text
[FASE 1: VENDEDOR PREPARA LA TIENDA]
  1. Registrar Vendedor  ->  2. Login Vendedor  ->  3. Registrar Bodega  ->  4. Crear Producto  ->  5. Ingresar Stock

[FASE 2: COMPRADOR HACE LA COMPRA]
  6. Registrar Comprador ->  7. Login Comprador ->  8. Crear Carrito    ->  9. Agregar Ítem    -> 10. Checkout  -> 11. Pagar

[FASE 3: OPERADOR LOGÍSTICO ENTREGA EL PAQUETE]
  12. Login Logística    -> 13. Despachar Pedido -> 14. Confirmar Entrega

[FASE 4: POSVENTA Y ADMINISTRACIÓN]
  15. Solicitar Reembolso (Comprador)  ->  16. Login Admin  ->  17. Suspender Usuario  ->  18. Reactivar Usuario
```

---

### Tabla Resumen: El Orden Exacto de Clics

| Paso | Carpeta en Postman | Petición a Abrir y Ejecutar | ¿Qué hace por debajo? | Variable que se guarda automáticamente |
| :---: | :--- | :--- | :--- | :--- |
| **1** | `1. Autenticación (Auth)` | **1.2 Registrar Vendedor** | Crea la cuenta y perfil comercial del vendedor | `{{sellerId}}` |
| **2** | `1. Autenticación (Auth)` | **1.4 Login Vendedor** | Inicia sesión del vendedor y genera su JWT | `{{sellerToken}}` |
| **3** | `4. Bodegas (Warehouses)` | **4.1 Registrar Bodega Física** | Crea la bodega física asociada a `{{sellerId}}` | `{{warehouseId}}` |
| **4** | `3. Catálogo de Productos` | **3.1 Crear Producto en Catálogo** | Publica el producto (PS5 Slim) asignado al vendedor | `{{sku}}` ("SKU-PS5-SLIM") |
| **5** | `5. Inventario Físico` | **5.1 Ingresar Stock Físico a Bodega** | Ingresa 30 unidades físicas a `{{warehouseId}}` | *Stock disponible = 30* |
| **6** | `1. Autenticación (Auth)` | **1.1 Registrar Comprador** | Registra el cliente (Carlos Ramírez) y dirección | `{{buyerId}}` |
| **7** | `1. Autenticación (Auth)` | **1.3 Login Comprador** | Inicia sesión del cliente y genera su JWT | `{{buyerToken}}` |
| **8** | `6. Carrito y Órdenes` | **6.1 Crear Carrito de Compras** | Abre una orden en estado `CART` para `{{buyerId}}` | `{{orderId}}` |
| **9** | `6. Carrito y Órdenes` | **6.2 Agregar Ítem al Carrito** | Agrega 2 unidades del `{{sku}}` al carrito | *Total = $1,399.98 USD* |
| **10** | `6. Carrito y Órdenes` | **6.4 Checkout de Orden** | Pasa a `PENDING_PAYMENT` y **reserva 2 unidades en bodega** | *Stock disponible = 28, reservado = 2* |
| **11** | `6. Carrito y Órdenes` | **6.5 Confirmar Pago de Orden** | Pasa a `PAID` y **consume definitivamente las 2 unidades** | *Stock físico = 28, reservado = 0* |
| **12** | `1. Autenticación (Auth)` | **1.6 Login Operador Logístico** | Autentica la cuenta maestra precargada (`logistics@nexus.com`) | `{{logisticsToken}}` |
| **13** | `7. Logística` | **7.1 Despachar Orden Pagada** | Registra transportadora y guía; pasa a `SHIPPED` | *Orden despachada* |
| **14** | `7. Logística` | **7.2 Confirmar Entrega al Comprador** | Confirma que el comprador recibió; pasa a `DELIVERED_FINALIZED` | *Ciclo de venta cerrado* |
| **15** | `8. Reembolsos` | **8.1 Procesar Reembolso y Devolución** | Pasa a `REFUNDED` y **reintegra automáticamente las 2 unidades a bodega** | *Stock físico vuelve a subir a 30* |
| **16** | `1. Autenticación (Auth)` | **1.5 Login Administrador** | Autentica la cuenta maestra precargada (`admin@nexus.com`) | `{{adminToken}}` |
| **17** | `2. Usuarios y Perfiles` | **2.3 Suspender Usuario (Admin)** | Bloquea la cuenta del vendedor `{{sellerId}}` | *Usuario pasa a inactivo* |
| **18** | `2. Usuarios y Perfiles` | **2.4 Reactivar Usuario (Admin)** | Reactiva la cuenta del vendedor | *Usuario vuelve a estar activo* |

---

## 3. Detalle de Carpetas en Postman y Peticiones Disponibles

### Carpeta 1: `1. Autenticación (Auth)`
Contiene los registros de nuevos usuarios y los inicios de sesión.
- **1.1 Registrar Comprador:** Crea un comprador con documento, dirección principal y clave. Guarda `buyerId`.
- **1.2 Registrar Vendedor:** Crea un comerciante con tienda y NIT. Guarda `sellerId`.
- **1.3 Login Comprador:** Valida correo y clave del comprador. Guarda `buyerToken`.
- **1.4 Login Vendedor:** Valida correo y clave del vendedor. Guarda `sellerToken`.
- **1.5 Login Administrador (Cuenta Pre-cargada):** Se loguea como `admin@nexus.com` (Clave: `Admin123!`). Guarda `adminToken`.
- **1.6 Login Operador Logístico (Cuenta Pre-cargada):** Se loguea como `logistics@nexus.com` (Clave: `Logistics123!`). Guarda `logisticsToken`.

---

### Carpeta 2: `2. Usuarios y Perfiles (Users & Profiles)`
Operaciones de perfil y gobierno de cuentas.
- **2.1 Agregar Dirección Secundaria:** Usa `{{buyerToken}}` y agrega otra dirección al perfil de `{{buyerId}}`.
- **2.2 Actualizar Tax ID de Vendedor:** Usa `{{sellerToken}}` y actualiza el RUT de `{{sellerId}}`.
- **2.3 Suspender Usuario (Admin):** Usa `{{adminToken}}` para suspender preventivamente a `{{sellerId}}`.
- **2.4 Reactivar Usuario (Admin):** Usa `{{adminToken}}` para reactivar a `{{sellerId}}`.

---

### Carpeta 3: `3. Catálogo de Productos (Product Catalog)`
Publicación y mantenimiento de productos.
- **3.1 Crear Producto en Catálogo:** Usa `{{sellerToken}}`. Publica la consola con SKU `SKU-PS5-SLIM`, precio `$650.00` y variante base. Guarda `sku`.
- **3.2 Actualizar Descripción y Precio:** Usa `{{sellerToken}}`. Cambia el precio a `$699.99` y ajusta la descripción del bundle.
- **3.3 Agregar Variante Comercial:** Usa `{{sellerToken}}`. Agrega la variante *"Bundle con 2 Controles DualSense"*.
- **3.4 Consultar Producto por SKU:** Consulta pública/autenticada para ver la ficha técnica del producto `{{sku}}`.
- **3.5 Listar Productos Activos:** Retorna todos los productos disponibles en la tienda.
- **3.6 Desactivar Producto (Borrado Lógico):** Usa `{{sellerToken}}`. Desactiva el producto para que no pueda ser comprado.

---

### Carpeta 4: `4. Bodegas (Warehouses)`
Administración de centros de almacenamiento.
- **4.1 Registrar Bodega Física:** Usa `{{sellerToken}}`. Registra la bodega del vendedor en Bogotá. Guarda `warehouseId`.
- **4.2 Desactivar Bodega Física:** Usa `{{sellerToken}}`. Apaga operativamente la bodega `{{warehouseId}}`.

---

### Carpeta 5: `5. Inventario Físico (Inventory)`
Control de existencias y mermas.
- **5.1 Ingresar Stock Físico a Bodega:** Usa `{{sellerToken}}`. Registra 30 unidades del `{{sku}}` en pasillo `P1`, estante `E3`, nivel `N2` de la bodega `{{warehouseId}}`.
- **5.2 Reportar Unidades Dañadas / Mermas:** Usa `{{sellerToken}}`. Reporta 2 unidades averiadas. Automáticamente el stock disponible se reduce de 30 a 28 unidades.
- **5.3 Consultar Stock Disponible Consolidado:** Retorna la cantidad neta disponible para la venta del SKU (`availableQuantity: 28`).

---

### Carpeta 6: `6. Carrito y Órdenes (Cart & Orders)`
El corazón transaccional del comercio.
- **6.1 Crear Carrito de Compras:** Usa `{{buyerToken}}`. Abre una orden vacía para `{{buyerId}}`. Guarda `orderId`.
- **6.2 Agregar Ítem al Carrito:** Usa `{{buyerToken}}`. Agrega 2 unidades de `{{sku}}`. El carrito calcula el subtotal y total automáticamente.
- **6.3 Eliminar Ítem del Carrito:** *(Opcional)* Quita un producto del carrito y recalcula a cero.
- **6.4 Checkout de Orden (Reserva Atómica):** Usa `{{buyerToken}}`. Fija la dirección de entrega, valida inventario disponible y **bloquea/reserva 2 unidades** en la bodega. Pasa a `PENDING_PAYMENT`.
- **6.5 Confirmar Pago de Orden (Deducción Definitiva):** Usa `{{buyerToken}}`. Simula el pago exitoso (`TRANS-PSE-9876543210`), deduce definitivamente las unidades del stock físico y pasa a `PAID`.
- **6.6 Cancelar Orden (Liberación de Reservas):** *(Flujo alternativo)* Si se cancela antes de pagar, libera inmediatamente las unidades reservadas en bodega para que otros clientes puedan comprarlas.

---

### Carpeta 7: `7. Logística (Logistics)`
Despacho y entrega física.
- **7.1 Despachar Orden Pagada:** Usa `{{logisticsToken}}`. Asigna guía de transporte (`ENV-SERVI-2026-9900`), transportadora (`Servientrega Express`) y pasa a `SHIPPED`.
- **7.2 Confirmar Entrega al Comprador:** Usa `{{logisticsToken}}`. Registra la firma/nombre de quien recibió el paquete y finaliza el pedido como `DELIVERED_FINALIZED`.

---

### Carpeta 8: `8. Reembolsos (Refunds)`
Garantías y devoluciones.
- **8.1 Procesar Reembolso y Devolución:** Usa `{{buyerToken}}`. Registra el motivo de garantía. Cambia el estado a `REFUNDED` y **reintegra automáticamente las 2 unidades devueltas al inventario físico de la bodega**.

---

## 4. Preguntas Frecuentes al Probar en Postman

#### ¿Qué pasa si le doy a *Checkout* antes de agregar ítems?
El sistema te devolverá un error `400 Bad Request`: `"No se puede hacer checkout a un carrito vacio"`.

#### ¿Qué pasa si intento comprar más unidades de las que hay en stock?
El servicio de dominio `InventoryReservationDomainService` bloqueará la transacción con un error `400 Bad Request` indicando que no hay suficiente disponibilidad física no reservada.

#### ¿Por qué el Login de Administrador ya funciona sin registrarlo antes?
Porque la aplicación incluye un componente [`DataInitializer`](file:///c:/Users/Usuario/Desktop/Nueva%20carpeta/Nexus_MarketPlace/src/main/java/application/config/DataInitializer.java) que al arrancar crea automáticamente en la base de datos las cuentas `admin@nexus.com` y `logistics@nexus.com` para que no tengas que crearlas por consola SQL.

#### ¿Dónde veo el valor actual de las variables en Postman?
En Postman, haz clic sobre el nombre de la colección **NexusMarket API** en el panel izquierdo y entra en la pestaña **Variables**. Verás cómo se van rellenando automáticamente con cada petición que ejecutas.

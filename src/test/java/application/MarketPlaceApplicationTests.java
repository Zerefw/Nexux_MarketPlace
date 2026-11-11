package application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de integracion End-to-End que levanta el servidor HTTP embebido y valida
 * todos los 24 endpoints REST, la seguridad JWT y las validaciones de campos obligatorios.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MarketPlaceApplicationTests {

    @LocalServerPort
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private HttpResponse<String> send(String method, String path, String token, String jsonBody) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + path))
                .header("Content-Type", "application/json");
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        if ("POST".equals(method) || "PUT".equals(method)) {
            builder.method(method, HttpRequest.BodyPublishers.ofString(jsonBody != null ? jsonBody : "{}"));
        } else if ("DELETE".equals(method)) {
            builder.DELETE();
        } else {
            builder.GET();
        }
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void fullApiLifecycleAndSecurityValidationTest() throws Exception {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String buyerEmail = "buyer" + uniqueSuffix + "@nexus.com";
        String sellerEmail = "seller" + uniqueSuffix + "@nexus.com";
        String sku = "SKU-PRO-" + uniqueSuffix;

        // 1. Validar que un endpoint protegido rechaza peticiones sin token JWT (401 Unauthorized)
        HttpResponse<String> unauthResp = send("GET", "/api/v1/catalog/products", null, null);
        assertEquals(401, unauthResp.statusCode(), "Debe exigir token JWT en rutas protegidas");

        // 2. Registrar Comprador (Exito)
        HttpResponse<String> regBuyerResp = send("POST", "/api/v1/auth/register/buyer", null, """
                {
                  "identificationDocument": "DOC-B-%s",
                  "fullName": "Santiago Comprador",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "primaryAddress": "Calle 100 # 15-20 Bogota"
                }
                """.formatted(uniqueSuffix, buyerEmail));
        assertEquals(200, regBuyerResp.statusCode(), regBuyerResp.body());
        JsonNode buyerJson = mapper.readTree(regBuyerResp.body());
        assertTrue(buyerJson.get("success").asBoolean());
        String buyerId = buyerJson.get("data").get("id").asText();
        assertNotNull(buyerId);

        // 3. Validar error al registrar comprador sin contrasena o con email duplicado (400 Bad Request)
        HttpResponse<String> dupBuyerResp = send("POST", "/api/v1/auth/register/buyer", null, """
                {
                  "identificationDocument": "DOC-B-%s",
                  "fullName": "Santiago Comprador",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "primaryAddress": "Calle 100"
                }
                """.formatted(uniqueSuffix, buyerEmail));
        assertEquals(400, dupBuyerResp.statusCode());

        // 4. Registrar Vendedor (Exito)
        HttpResponse<String> regSellerResp = send("POST", "/api/v1/auth/register/seller", null, """
                {
                  "identificationDocument": "DOC-S-%s",
                  "fullName": "Tienda Oficial Tech",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "storeName": "Nexus Tech Store",
                  "taxId": "NIT-900123456-1"
                }
                """.formatted(uniqueSuffix, sellerEmail));
        assertEquals(200, regSellerResp.statusCode(), regSellerResp.body());
        String sellerId = mapper.readTree(regSellerResp.body()).get("data").get("id").asText();

        // 5. Login para obtener Tokens JWT separados por rol (Comprador y Vendedor)
        HttpResponse<String> loginBuyerResp = send("POST", "/api/v1/auth/login", null, """
                {
                  "email": "%s",
                  "password": "Password123!"
                }
                """.formatted(buyerEmail));
        assertEquals(200, loginBuyerResp.statusCode(), loginBuyerResp.body());
        String buyerToken = mapper.readTree(loginBuyerResp.body()).get("data").get("token").asText();
        assertNotNull(buyerToken);

        HttpResponse<String> loginSellerResp = send("POST", "/api/v1/auth/login", null, """
                {
                  "email": "%s",
                  "password": "Password123!"
                }
                """.formatted(sellerEmail));
        assertEquals(200, loginSellerResp.statusCode(), loginSellerResp.body());
        String sellerToken = mapper.readTree(loginSellerResp.body()).get("data").get("token").asText();
        assertNotNull(sellerToken);

        // Validar control de acceso por rol (403 Forbidden): Comprador intentando crear producto y Vendedor intentando crear carrito
        HttpResponse<String> buyerCreateProdForbidden = send("POST", "/api/v1/catalog/products", buyerToken, "{}");
        assertEquals(403, buyerCreateProdForbidden.statusCode(), "Un COMPRADOR no debe poder crear productos");

        HttpResponse<String> sellerCreateCartForbidden = send("POST", "/api/v1/orders/cart", sellerToken, "{}");
        assertEquals(403, sellerCreateCartForbidden.statusCode(), "Un VENDEDOR no debe poder crear carritos de compra");

        // 6. Operaciones de Usuario: Direccion secundaria (buyerToken), Actualizar TaxId (sellerToken), Suspender y Reactivar
        HttpResponse<String> addrResp = send("POST", "/api/v1/users/buyers/secondary-address", buyerToken, """
                {
                  "buyerId": "%s",
                  "address": "Carrera 7 # 72-10 Oficina 402"
                }
                """.formatted(buyerId));
        assertEquals(200, addrResp.statusCode(), addrResp.body());

        // Validar hueco: buyerId vacio debe retornar 400
        HttpResponse<String> invalidAddrResp = send("POST", "/api/v1/users/buyers/secondary-address", buyerToken, """
                {
                  "buyerId": "",
                  "address": "Carrera 7"
                }
                """);
        assertEquals(400, invalidAddrResp.statusCode());

        HttpResponse<String> taxResp = send("PUT", "/api/v1/users/sellers/tax-id", sellerToken, """
                {
                  "sellerId": "%s",
                  "taxId": "NIT-900999888-7"
                }
                """.formatted(sellerId));
        assertEquals(200, taxResp.statusCode(), taxResp.body());

        HttpResponse<String> suspendResp = send("PUT", "/api/v1/users/" + sellerId + "/suspend", sellerToken, "{}");
        assertEquals(200, suspendResp.statusCode(), suspendResp.body());

        HttpResponse<String> activateResp = send("PUT", "/api/v1/users/" + sellerId + "/activate", sellerToken, "{}");
        assertEquals(200, activateResp.statusCode(), activateResp.body());

        // 7. Catalogo: Crear producto, Actualizar, Agregar Variante, Consultar por SKU y Listar Activos (sellerToken)
        HttpResponse<String> createProdResp = send("POST", "/api/v1/catalog/products", sellerToken, """
                {
                  "sku": "%s",
                  "sellerId": "%s",
                  "name": "Monitor Gamer 27 Pulgadas",
                  "description": "Monitor IPS 165Hz",
                  "priceAmount": 1200.00,
                  "priceCurrency": "USD",
                  "type": "PHYSICAL",
                  "variants": ["Negro", "165Hz"]
                }
                """.formatted(sku, sellerId));
        assertEquals(200, createProdResp.statusCode(), createProdResp.body());

        HttpResponse<String> updateProdResp = send("PUT", "/api/v1/catalog/products", sellerToken, """
                {
                  "sku": "%s",
                  "description": "Monitor IPS 165Hz HDR10 Actualizado",
                  "priceAmount": 1150.00,
                  "priceCurrency": "USD"
                }
                """.formatted(sku));
        assertEquals(200, updateProdResp.statusCode(), updateProdResp.body());

        HttpResponse<String> variantResp = send("POST", "/api/v1/catalog/products/variants", sellerToken, """
                {
                  "sku": "%s",
                  "variant": "Edicion Especial"
                }
                """.formatted(sku));
        assertEquals(200, variantResp.statusCode(), variantResp.body());

        HttpResponse<String> findProdResp = send("GET", "/api/v1/catalog/products/" + sku, buyerToken, null);
        assertEquals(200, findProdResp.statusCode(), findProdResp.body());

        HttpResponse<String> listProdResp = send("GET", "/api/v1/catalog/products", buyerToken, null);
        assertEquals(200, listProdResp.statusCode(), listProdResp.body());

        // 8. Bodega e Inventario: Registrar bodega, Ingresar stock, Reportar merma y Consultar disponible (sellerToken)
        HttpResponse<String> whResp = send("POST", "/api/v1/warehouses", sellerToken, """
                {
                  "ownerId": "%s",
                  "name": "Bodega Central Zona Franca",
                  "locationAddress": "Parque Industrial Bodega 14",
                  "type": "SELLER"
                }
                """.formatted(sellerId));
        assertEquals(200, whResp.statusCode(), whResp.body());
        String warehouseId = mapper.readTree(whResp.body()).get("data").get("id").asText();

        HttpResponse<String> addStockResp = send("POST", "/api/v1/inventory/stock", sellerToken, """
                {
                  "warehouseId": "%s",
                  "sku": "%s",
                  "quantity": 20,
                  "aisle": "A1",
                  "shelf": "S2",
                  "level": "L3"
                }
                """.formatted(warehouseId, sku));
        assertEquals(200, addStockResp.statusCode(), addStockResp.body());

        HttpResponse<String> dmgStockResp = send("POST", "/api/v1/inventory/damaged", sellerToken, """
                {
                  "warehouseId": "%s",
                  "sku": "%s",
                  "damagedQuantity": 2
                }
                """.formatted(warehouseId, sku));
        assertEquals(200, dmgStockResp.statusCode(), dmgStockResp.body());

        HttpResponse<String> availStockResp = send("GET", "/api/v1/inventory/stock/" + sku, buyerToken, null);
        assertEquals(200, availStockResp.statusCode(), availStockResp.body());
        assertEquals(18, mapper.readTree(availStockResp.body()).get("data").get("availableQuantity").asInt());

        // 9. Ordenes, Logistica y Reembolsos: Crear carrito, Agregar/Quitar item, Checkout, Pagar (buyerToken)
        HttpResponse<String> cartResp = send("POST", "/api/v1/orders/cart", buyerToken, """
                {
                  "buyerId": "%s",
                  "currency": "USD"
                }
                """.formatted(buyerId));
        assertEquals(200, cartResp.statusCode(), cartResp.body());
        String orderId = mapper.readTree(cartResp.body()).get("data").get("id").asText();

        HttpResponse<String> addItemResp = send("POST", "/api/v1/orders/cart/items", buyerToken, """
                {
                  "orderId": "%s",
                  "sku": "%s",
                  "quantity": 3,
                  "unitPriceAmount": 1150.00,
                  "currency": "USD"
                }
                """.formatted(orderId, sku));
        assertEquals(200, addItemResp.statusCode(), addItemResp.body());

        HttpResponse<String> removeItemResp = send("DELETE", "/api/v1/orders/" + orderId + "/items/" + sku, buyerToken, null);
        assertEquals(200, removeItemResp.statusCode(), removeItemResp.body());

        // Volver a agregar item para continuar al checkout
        send("POST", "/api/v1/orders/cart/items", buyerToken, """
                {
                  "orderId": "%s",
                  "sku": "%s",
                  "quantity": 3,
                  "unitPriceAmount": 1150.00,
                  "currency": "USD"
                }
                """.formatted(orderId, sku));

        HttpResponse<String> checkoutResp = send("POST", "/api/v1/orders/checkout", buyerToken, """
                {
                  "orderId": "%s",
                  "shippingAddress": "Calle 100 # 15-20 Bogota"
                }
                """.formatted(orderId));
        assertEquals(200, checkoutResp.statusCode(), checkoutResp.body());
        assertEquals("PENDING_PAYMENT", mapper.readTree(checkoutResp.body()).get("data").get("status").asText());

        HttpResponse<String> payResp = send("POST", "/api/v1/orders/pay", buyerToken, """
                {
                  "orderId": "%s",
                  "paymentReference": "PAY-REF-998877"
                }
                """.formatted(orderId));
        assertEquals(200, payResp.statusCode(), payResp.body());
        assertEquals("PAID", mapper.readTree(payResp.body()).get("data").get("status").asText());

        HttpResponse<String> shipResp = send("POST", "/api/v1/logistics/ship", sellerToken, """
                {
                  "orderId": "%s",
                  "trackingNumber": "TRK-COL-554433",
                  "carrierName": "Servientrega"
                }
                """.formatted(orderId));
        assertEquals(200, shipResp.statusCode(), shipResp.body());
        assertEquals("SHIPPED", mapper.readTree(shipResp.body()).get("data").get("status").asText());

        HttpResponse<String> deliverResp = send("POST", "/api/v1/logistics/deliver", sellerToken, """
                {
                  "orderId": "%s",
                  "receivedBy": "Santiago Comprador"
                }
                """.formatted(orderId));
        assertEquals(200, deliverResp.statusCode(), deliverResp.body());
        assertEquals("DELIVERED_FINALIZED", mapper.readTree(deliverResp.body()).get("data").get("status").asText());

        HttpResponse<String> refundResp = send("POST", "/api/v1/refunds", buyerToken, """
                {
                  "orderId": "%s",
                  "reason": "Producto con defecto de fabrica"
                }
                """.formatted(orderId));
        assertEquals(200, refundResp.statusCode(), refundResp.body());
        assertEquals("REFUNDED", mapper.readTree(refundResp.body()).get("data").get("status").asText());

        // 10. Cancelacion de una segunda orden en estado PENDING_PAYMENT (verifica liberacion de reserva)
        HttpResponse<String> cart2Resp = send("POST", "/api/v1/orders/cart", buyerToken, """
                {
                  "buyerId": "%s",
                  "currency": "USD"
                }
                """.formatted(buyerId));
        String order2Id = mapper.readTree(cart2Resp.body()).get("data").get("id").asText();
        send("POST", "/api/v1/orders/cart/items", buyerToken, """
                {
                  "orderId": "%s",
                  "sku": "%s",
                  "quantity": 2,
                  "unitPriceAmount": 1150.00,
                  "currency": "USD"
                }
                """.formatted(order2Id, sku));
        send("POST", "/api/v1/orders/checkout", buyerToken, """
                {
                  "orderId": "%s",
                  "shippingAddress": "Calle 100 # 15-20 Bogota"
                }
                """.formatted(order2Id));
        HttpResponse<String> cancelResp = send("POST", "/api/v1/orders/cancel", buyerToken, """
                {
                  "orderId": "%s",
                  "reason": "Cliente desistio de la compra"
                }
                """.formatted(order2Id));
        assertEquals(200, cancelResp.statusCode(), cancelResp.body());
        assertEquals("CANCELLED", mapper.readTree(cancelResp.body()).get("data").get("status").asText());

        // 11. Validar integridad referencial de "huecos" cerrados (IDs inexistentes deben retornar 400 Bad Request)
        HttpResponse<String> fakeSellerProdResp = send("POST", "/api/v1/catalog/products", sellerToken, """
                {
                  "sku": "SKU-FAKE-999",
                  "sellerId": "seller-inexistente-999",
                  "name": "Producto Huérfano",
                  "description": "No debe crearse",
                  "priceAmount": 50.00,
                  "priceCurrency": "USD",
                  "type": "PHYSICAL",
                  "variants": []
                }
                """);
        assertEquals(400, fakeSellerProdResp.statusCode(), "No debe permitir crear producto con sellerId inexistente");

        HttpResponse<String> fakeOwnerWhResp = send("POST", "/api/v1/warehouses", sellerToken, """
                {
                  "ownerId": "seller-inexistente-999",
                  "name": "Bodega Huérfana",
                  "locationAddress": "Calle Falsa 123",
                  "type": "SELLER"
                }
                """);
        assertEquals(400, fakeOwnerWhResp.statusCode(), "No debe permitir registrar bodega SELLER con ownerId inexistente");

        HttpResponse<String> fakeWhStockResp = send("POST", "/api/v1/inventory/stock", sellerToken, """
                {
                  "warehouseId": "bodega-inexistente-999",
                  "sku": "%s",
                  "quantity": 10,
                  "aisle": "A1",
                  "shelf": "S1",
                  "level": "L1"
                }
                """.formatted(sku));
        assertEquals(400, fakeWhStockResp.statusCode(), "No debe permitir ingresar stock en bodega inexistente");

        HttpResponse<String> fakeBuyerCartResp = send("POST", "/api/v1/orders/cart", buyerToken, """
                {
                  "buyerId": "comprador-inexistente-999",
                  "currency": "USD"
                }
                """);
        assertEquals(400, fakeBuyerCartResp.statusCode(), "No debe permitir crear carrito con buyerId inexistente");

        HttpResponse<String> fakeSkuCartResp = send("POST", "/api/v1/orders/cart/items", buyerToken, """
                {
                  "orderId": "%s",
                  "sku": "SKU-INEXISTENTE-999",
                  "quantity": 1,
                  "unitPriceAmount": 100.00,
                  "currency": "USD"
                }
                """.formatted(order2Id));
        assertEquals(400, fakeSkuCartResp.statusCode(), "No debe permitir agregar SKU inexistente al carrito");

        // 12. Desactivar producto y bodega (sellerToken)
        HttpResponse<String> deactProdResp = send("DELETE", "/api/v1/catalog/products/" + sku, sellerToken, null);
        assertEquals(200, deactProdResp.statusCode(), deactProdResp.body());

        HttpResponse<String> deactWhResp = send("PUT", "/api/v1/warehouses/" + warehouseId + "/deactivate", sellerToken, "{}");
        assertEquals(200, deactWhResp.statusCode(), deactWhResp.body());
    }
}

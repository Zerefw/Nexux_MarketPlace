package application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas automatizadas especificamente enfocada en validar:
 * 1. Peticiones que exigen Token JWT ausente o invalido (401).
 * 2. Peticiones donde el ID de usuario no se envia o no es obligatorio (400).
 * 3. Identificacion y prueba de "huecos" logicos y de seguridad (IDOR, manipulacion de precio,
 *    falta de control de roles en suspension/logistica, token activo tras suspension).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityAndValidationGapsTest {

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

    private String login(String email, String password) throws Exception {
        HttpResponse<String> resp = send("POST", "/api/v1/auth/login", null, """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password));
        assertEquals(200, resp.statusCode(), "Login debe ser exitoso para: " + email);
        return mapper.readTree(resp.body()).get("data").get("token").asText();
    }

    @Test
    @DisplayName("1. Validar que endpoints protegidos rechazan peticiones sin token o con token corrupto (401)")
    void testEndpointsRequireToken() throws Exception {
        // Sin token
        HttpResponse<String> r1 = send("GET", "/api/v1/catalog/products", null, null);
        assertEquals(401, r1.statusCode(), "Catalogo protegido debe exigir token");

        HttpResponse<String> r2 = send("POST", "/api/v1/orders/cart", null, "{}");
        assertEquals(401, r2.statusCode(), "Crear carrito debe exigir token");

        HttpResponse<String> r3 = send("POST", "/api/v1/warehouses", null, "{}");
        assertEquals(401, r3.statusCode(), "Bodegas debe exigir token");

        HttpResponse<String> r4 = send("POST", "/api/v1/inventory/stock", null, "{}");
        assertEquals(401, r4.statusCode(), "Inventario debe exigir token");

        // Token invalido / corrupto
        HttpResponse<String> r5 = send("GET", "/api/v1/catalog/products", "token-completamente-invalido.123.456", null);
        assertEquals(401, r5.statusCode(), "Token corrupto debe retornar 401");
    }

    @Test
    @DisplayName("2. Validar que usuarios precargados (Admin y Logistics) pueden autenticarse")
    void testSeededUsersLogin() throws Exception {
        String adminToken = login("admin@nexus.com", "Admin123!");
        assertNotNull(adminToken);

        String logisticsToken = login("logistics@nexus.com", "Logistics123!");
        assertNotNull(logisticsToken);
    }

    @Test
    @DisplayName("3. Validar rechazo (400) cuando no se pasa un ID obligatorio en el cuerpo")
    void testMissingUserIdsInRequests() throws Exception {
        String adminToken = login("admin@nexus.com", "Admin123!");

        // a) buyerId ausente o vacio al crear carrito
        HttpResponse<String> noBuyerCart = send("POST", "/api/v1/orders/cart", adminToken, """
                {
                  "buyerId": null,
                  "currency": "USD"
                }
                """);
        assertEquals(400, noBuyerCart.statusCode());
        assertTrue(noBuyerCart.body().contains("buyerId"));

        HttpResponse<String> emptyBuyerCart = send("POST", "/api/v1/orders/cart", adminToken, """
                {
                  "buyerId": "",
                  "currency": "USD"
                }
                """);
        assertEquals(400, emptyBuyerCart.statusCode());

        // b) sellerId ausente al crear producto
        HttpResponse<String> noSellerProd = send("POST", "/api/v1/catalog/products", adminToken, """
                {
                  "sku": "SKU-TEST-NULL-SELLER",
                  "name": "Prueba",
                  "priceAmount": 10.0,
                  "priceCurrency": "USD",
                  "type": "PHYSICAL"
                }
                """);
        assertEquals(400, noSellerProd.statusCode());
        assertTrue(noSellerProd.body().contains("sellerId"));

        // c) ownerId ausente al registrar bodega
        HttpResponse<String> noOwnerWh = send("POST", "/api/v1/warehouses", adminToken, """
                {
                  "name": "Bodega Sin Owner",
                  "locationAddress": "Calle 1 # 2-3",
                  "type": "SELLER"
                }
                """);
        assertEquals(400, noOwnerWh.statusCode());
        assertTrue(noOwnerWh.body().contains("ownerId"));

        // d) buyerId ausente al agregar direccion secundaria
        HttpResponse<String> noBuyerAddr = send("POST", "/api/v1/users/buyers/secondary-address", adminToken, """
                {
                  "address": "Calle 50"
                }
                """);
        assertEquals(400, noBuyerAddr.statusCode());

        // e) sellerId ausente al actualizar taxId
        HttpResponse<String> noSellerTax = send("PUT", "/api/v1/users/sellers/tax-id", adminToken, """
                {
                  "taxId": "NIT-111222333"
                }
                """);
        assertEquals(400, noSellerTax.statusCode());
    }

    @Test
    @DisplayName("4. Demostracion de Hueco de Seguridad: Manipulacion de Precios en el Carrito")
    void testPriceTamperingVulnerabilityInCart() throws Exception {
        String suffix = String.valueOf(System.currentTimeMillis());
        String sellerEmail = "seller_price_" + suffix + "@nexus.com";
        String buyerEmail = "buyer_price_" + suffix + "@nexus.com";
        String sku = "SKU-EXPENSIVE-" + suffix;

        // Registrar vendedor
        HttpResponse<String> regSeller = send("POST", "/api/v1/auth/register/seller", null, """
                {
                  "identificationDocument": "DOC-SP-%s",
                  "fullName": "Vendedor Precios",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "storeName": "High End Store",
                  "taxId": "NIT-999000111"
                }
                """.formatted(suffix, sellerEmail));
        assertEquals(200, regSeller.statusCode());
        String sellerId = mapper.readTree(regSeller.body()).get("data").get("id").asText();
        String sellerToken = login(sellerEmail, "Password123!");

        // Registrar comprador
        HttpResponse<String> regBuyer = send("POST", "/api/v1/auth/register/buyer", null, """
                {
                  "identificationDocument": "DOC-BP-%s",
                  "fullName": "Comprador Astuto",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "primaryAddress": "Av Principal 123"
                }
                """.formatted(suffix, buyerEmail));
        assertEquals(200, regBuyer.statusCode());
        String buyerId = mapper.readTree(regBuyer.body()).get("data").get("id").asText();
        String buyerToken = login(buyerEmail, "Password123!");

        // Crear producto con precio oficial de 5000.00 USD
        HttpResponse<String> prodResp = send("POST", "/api/v1/catalog/products", sellerToken, """
                {
                  "sku": "%s",
                  "sellerId": "%s",
                  "name": "Laptop Alta Gama 5000 USD",
                  "description": "Precio real 5000",
                  "priceAmount": 5000.00,
                  "priceCurrency": "USD",
                  "type": "PHYSICAL"
                }
                """.formatted(sku, sellerId));
        assertEquals(200, prodResp.statusCode());

        // Comprador crea carrito
        HttpResponse<String> cartResp = send("POST", "/api/v1/orders/cart", buyerToken, """
                {
                  "buyerId": "%s",
                  "currency": "USD"
                }
                """.formatted(buyerId));
        assertEquals(200, cartResp.statusCode());
        String orderId = mapper.readTree(cartResp.body()).get("data").get("id").asText();

        // HUECO: El comprador anade el item pero altera unitPriceAmount a 1.00 USD
        HttpResponse<String> tamperResp = send("POST", "/api/v1/orders/cart/items", buyerToken, """
                {
                  "orderId": "%s",
                  "sku": "%s",
                  "quantity": 1,
                  "unitPriceAmount": 1.00,
                  "currency": "USD"
                }
                """.formatted(orderId, sku));
        assertEquals(200, tamperResp.statusCode());

        JsonNode orderData = mapper.readTree(tamperResp.body()).get("data");
        double chargedTotal = orderData.get("totalAmount").get("amount").asDouble();

        // El sistema actualmente acepta el precio manipulado del cliente en vez del precio del catalogo (5000.00)
        assertEquals(1.0, chargedTotal, "HUECO CONFIRMADO: El carrito acepto el precio manipulado de 1.0 en lugar de 5000.0");
    }

    @Test
    @DisplayName("5. Demostracion de Hueco de Seguridad: IDOR entre usuarios en creacion de carrito")
    void testIdorInCartCreation() throws Exception {
        String suffix = String.valueOf(System.currentTimeMillis());
        String buyer1Email = "b1_" + suffix + "@nexus.com";
        String buyer2Email = "b2_" + suffix + "@nexus.com";

        // Registrar Comprador 1 y 2
        HttpResponse<String> r1 = send("POST", "/api/v1/auth/register/buyer", null, """
                {
                  "identificationDocument": "ID-B1-%s",
                  "fullName": "Comprador 1",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "primaryAddress": "Calle 1"
                }
                """.formatted(suffix, buyer1Email));
        String buyer1Id = mapper.readTree(r1.body()).get("data").get("id").asText();
        String buyer1Token = login(buyer1Email, "Password123!");

        HttpResponse<String> r2 = send("POST", "/api/v1/auth/register/buyer", null, """
                {
                  "identificationDocument": "ID-B2-%s",
                  "fullName": "Comprador 2",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "primaryAddress": "Calle 2"
                }
                """.formatted(suffix, buyer2Email));
        String buyer2Id = mapper.readTree(r2.body()).get("data").get("id").asText();

        // HUECO: Comprador 1 usa su token pero envia el buyerId del Comprador 2
        HttpResponse<String> cartIdor = send("POST", "/api/v1/orders/cart", buyer1Token, """
                {
                  "buyerId": "%s",
                  "currency": "USD"
                }
                """.formatted(buyer2Id));

        // El sistema actualmente crea el carrito a nombre del comprador 2 sin verificar que el token pertenece a comprador 1
        assertEquals(200, cartIdor.statusCode());
        String createdBuyerId = mapper.readTree(cartIdor.body()).get("data").get("buyerId").asText();
        assertEquals(buyer2Id, createdBuyerId, "HUECO CONFIRMADO: Comprador 1 creo una orden a nombre de Comprador 2");
    }

    @Test
    @DisplayName("6. Demostracion de Hueco de Roles: Usuario comun puede suspender a otro usuario")
    void testNormalUserCanSuspendOtherUsers() throws Exception {
        String suffix = String.valueOf(System.currentTimeMillis());
        String buyerEmail = "victim_buyer_" + suffix + "@nexus.com";
        String rogueBuyerEmail = "rogue_buyer_" + suffix + "@nexus.com";

        HttpResponse<String> r1 = send("POST", "/api/v1/auth/register/buyer", null, """
                {
                  "identificationDocument": "ID-V-%s",
                  "fullName": "Victima",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "primaryAddress": "Calle 1"
                }
                """.formatted(suffix, buyerEmail));
        String victimId = mapper.readTree(r1.body()).get("data").get("id").asText();

        HttpResponse<String> r2 = send("POST", "/api/v1/auth/register/buyer", null, """
                {
                  "identificationDocument": "ID-R-%s",
                  "fullName": "Rogue Buyer",
                  "email": "%s",
                  "rawPassword": "Password123!",
                  "primaryAddress": "Calle 2"
                }
                """.formatted(suffix, rogueBuyerEmail));
        String rogueToken = login(rogueBuyerEmail, "Password123!");

        // HUECO: Rogue Buyer (rol BUYER) suspende a la victima porque la ruta no exige rol ADMIN
        HttpResponse<String> suspendResp = send("PUT", "/api/v1/users/" + victimId + "/suspend", rogueToken, "{}");
        assertEquals(200, suspendResp.statusCode(), "HUECO CONFIRMADO: Un usuario con rol BUYER logro suspender a otro usuario");

        // Reactivamos con Admin
        String adminToken = login("admin@nexus.com", "Admin123!");
        HttpResponse<String> activateResp = send("PUT", "/api/v1/users/" + victimId + "/activate", adminToken, "{}");
        assertEquals(200, activateResp.statusCode());
    }
}

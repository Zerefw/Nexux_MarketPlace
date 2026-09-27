// Paquete para los comandos de entrada (DTOs)
package application.port.in.command;

// Importa las anotaciones de Lombok para generar constructores y getters
import lombok.Builder;
import lombok.Value;

// Importa la lista genérica de Java
import java.util.List;
// Importa la clase BigDecimal para manejar dinero
import java.math.BigDecimal;

// @Value hace que la clase sea inmutable (propiedades 'final')
@Value
// @Builder facilita la construcción fluida de objetos
@Builder
public class CreateProductCommand {
    // Código SKU único del producto
    String sku;
    // Identificador del vendedor que crea el producto
    String sellerId;
    // Nombre visible del producto
    String name;
    // Descripción comercial del producto
    String description;
    // Monto exacto del precio
    BigDecimal priceAmount;
    // Moneda del precio (ej. "USD", "COP")
    String priceCurrency;
    // Tipo de producto en texto ("PHYSICAL" o "DIGITAL")
    String type;
    // Lista opcional de variantes (talla, color)
    List<String> variants;
}

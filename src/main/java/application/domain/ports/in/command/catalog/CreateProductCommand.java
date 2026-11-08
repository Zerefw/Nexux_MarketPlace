package application.domain.ports.in.command.catalog;

import java.math.BigDecimal;
import java.util.List;

/**
 * Comando para la creacion de un nuevo producto en el catalogo.
 *
 * @param sku           Codigo SKU unico del producto.
 * @param sellerId      Identificador del vendedor propietario.
 * @param name          Nombre comercial del producto.
 * @param description   Descripcion detallada del producto.
 * @param priceAmount   Valor monetario del precio unitario.
 * @param priceCurrency Codigo ISO de la moneda.
 * @param type          Tipo de producto (PHYSICAL o DIGITAL).
 * @param variants      Lista opcional de variantes iniciales.
 */
public record CreateProductCommand(
        String sku,
        String sellerId,
        String name,
        String description,
        BigDecimal priceAmount,
        String priceCurrency,
        String type,
        List<String> variants
) {
    public String getSku() { return sku; }
    public String getSellerId() { return sellerId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPriceAmount() { return priceAmount; }
    public String getPriceCurrency() { return priceCurrency; }
    public String getType() { return type; }
    public List<String> getVariants() { return variants; }
}

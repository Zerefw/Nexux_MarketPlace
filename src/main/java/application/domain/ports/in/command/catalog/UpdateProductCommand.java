package application.domain.ports.in.command.catalog;

import java.math.BigDecimal;

/**
 * Comando para actualizar los atributos descriptivos y el precio de un producto.
 *
 * @param sku           Codigo SKU del producto a modificar.
 * @param description   Nueva descripcion del producto.
 * @param priceAmount   Nuevo valor monetario del precio.
 * @param priceCurrency Codigo ISO de la moneda.
 */
public record UpdateProductCommand(
        String sku,
        String description,
        BigDecimal priceAmount,
        String priceCurrency
) {
}

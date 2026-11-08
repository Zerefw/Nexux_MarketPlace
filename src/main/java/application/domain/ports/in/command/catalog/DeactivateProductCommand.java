package application.domain.ports.in.command.catalog;

/**
 * Comando para desactivar logicamente un producto del catalogo.
 *
 * @param sku Codigo SKU del producto a desactivar.
 */
public record DeactivateProductCommand(String sku) {
}

package application.domain.ports.in.command.inventory;

/**
 * Comando para consultar el stock disponible consolidado de un SKU.
 *
 * @param sku Codigo SKU del producto a consultar.
 */
public record GetAvailableStockCommand(String sku) {
}

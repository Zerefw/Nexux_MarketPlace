package application.domain.ports.in.command.catalog;

/**
 * Comando para consultar un producto especifico mediante su SKU.
 *
 * @param sku Codigo SKU a consultar.
 */
public record FindProductBySkuCommand(String sku) {
}

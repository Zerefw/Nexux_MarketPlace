package application.domain.ports.in.command.catalog;

/**
 * Comando para agregar una nueva variante comercial a un producto existente.
 *
 * @param sku     Codigo SKU del producto base.
 * @param variant Nombre o descripcion de la variante (por ejemplo, talla o color).
 */
public record AddProductVariantCommand(String sku, String variant) {
}

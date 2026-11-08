package application.domain.ports.in.command.inventory;

/**
 * Comando para reportar unidades danadas o mermas en una bodega especifica.
 *
 * @param warehouseId     Identificador de la bodega donde ocurrio la merma.
 * @param sku             Codigo SKU del producto afectado.
 * @param damagedQuantity Cantidad de unidades danadas a reportar.
 */
public record ReportDamagedStockCommand(
        String warehouseId,
        String sku,
        int damagedQuantity
) {
}

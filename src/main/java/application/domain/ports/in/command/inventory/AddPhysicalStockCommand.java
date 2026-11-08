package application.domain.ports.in.command.inventory;

/**
 * Comando para registrar el ingreso fisico de mercancia en una bodega.
 *
 * @param warehouseId Identificador de la bodega receptora.
 * @param sku         Codigo SKU del producto ingresado.
 * @param quantity    Cantidad fisica de unidades a ingresar.
 * @param aisle       Pasillo de ubicacion dentro de la bodega.
 * @param shelf       Estante de ubicacion dentro de la bodega.
 * @param level       Nivel de ubicacion en el estante.
 */
public record AddPhysicalStockCommand(
        String warehouseId,
        String sku,
        int quantity,
        String aisle,
        String shelf,
        String level
) {
}

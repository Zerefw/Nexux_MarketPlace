package application.domain.ports.in.command.warehouse;

/**
 * Comando para desactivar una bodega fisica existente.
 *
 * @param warehouseId Identificador unico de la bodega.
 */
public record DeactivateWarehouseCommand(String warehouseId) {
}

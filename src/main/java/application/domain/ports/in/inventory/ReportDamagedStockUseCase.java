package application.domain.ports.in.inventory;

import application.domain.model.entity.InventoryItem;
import application.domain.ports.in.command.inventory.ReportDamagedStockCommand;

/**
 * Puerto de entrada para el caso de uso de reporte de mermas o stock danado.
 */
public interface ReportDamagedStockUseCase {

    /**
     * Registra unidades danadas descontandolas de la disponibilidad para venta.
     *
     * @param command Comando con la bodega, el SKU y la cantidad danada.
     * @return Entidad InventoryItem actualizada.
     */
    InventoryItem execute(ReportDamagedStockCommand command);
}

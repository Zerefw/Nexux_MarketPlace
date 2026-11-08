package application.domain.ports.in.inventory;

import application.domain.model.entity.InventoryItem;
import application.domain.ports.in.command.inventory.AddPhysicalStockCommand;

/**
 * Puerto de entrada para el caso de uso de ingreso de stock fisico.
 */
public interface AddPhysicalStockUseCase {

    /**
     * Registra la entrada de unidades fisicas al inventario de una bodega.
     *
     * @param command Comando con los datos de bodega, SKU, cantidad y ubicacion.
     * @return Entidad InventoryItem creada o actualizada.
     */
    InventoryItem execute(AddPhysicalStockCommand command);
}

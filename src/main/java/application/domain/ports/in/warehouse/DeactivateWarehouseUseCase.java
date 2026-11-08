package application.domain.ports.in.warehouse;

import application.domain.ports.in.command.warehouse.DeactivateWarehouseCommand;

/**
 * Puerto de entrada para el caso de uso de desactivacion de bodega.
 */
public interface DeactivateWarehouseUseCase {

    /**
     * Desactiva una bodega fisica existente.
     *
     * @param command Comando con el identificador de la bodega.
     */
    void execute(DeactivateWarehouseCommand command);
}

package application.port.in;

import application.port.in.command.DeactivateWarehouseCommand;

/**
 * Puerto de entrada para el caso de uso DeactivateWarehouse.
 */
public interface DeactivateWarehouseUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(DeactivateWarehouseCommand command);
}

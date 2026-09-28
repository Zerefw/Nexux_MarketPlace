package application.domain.ports.in;

import application.domain.ports.in.command.DeactivateWarehouseCommand;

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

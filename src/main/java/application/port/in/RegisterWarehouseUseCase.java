package application.port.in;

import application.port.in.command.RegisterWarehouseCommand;

/**
 * Puerto de entrada para el caso de uso RegisterWarehouse.
 */
public interface RegisterWarehouseUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(RegisterWarehouseCommand command);
}

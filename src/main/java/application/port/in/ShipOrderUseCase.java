package application.port.in;

import application.port.in.command.ShipOrderCommand;

/**
 * Puerto de entrada para el caso de uso ShipOrder.
 */
public interface ShipOrderUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(ShipOrderCommand command);
}

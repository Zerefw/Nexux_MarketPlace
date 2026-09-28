package application.domain.ports.in;

import application.domain.ports.in.command.CancelOrderCommand;

/**
 * Puerto de entrada para el caso de uso CancelOrder.
 */
public interface CancelOrderUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(CancelOrderCommand command);
}

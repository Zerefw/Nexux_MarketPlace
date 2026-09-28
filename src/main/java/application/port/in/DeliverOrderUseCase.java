package application.port.in;

import application.port.in.command.DeliverOrderCommand;

/**
 * Puerto de entrada para el caso de uso DeliverOrder.
 */
public interface DeliverOrderUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(DeliverOrderCommand command);
}

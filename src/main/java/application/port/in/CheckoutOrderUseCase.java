package application.port.in;

import application.port.in.command.CheckoutOrderCommand;

/**
 * Puerto de entrada para el caso de uso CheckoutOrder.
 */
public interface CheckoutOrderUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(CheckoutOrderCommand command);
}

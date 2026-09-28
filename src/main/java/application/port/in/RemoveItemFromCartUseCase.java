package application.port.in;

import application.port.in.command.RemoveItemFromCartCommand;

/**
 * Puerto de entrada para el caso de uso RemoveItemFromCart.
 */
public interface RemoveItemFromCartUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(RemoveItemFromCartCommand command);
}

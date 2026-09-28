package application.port.in;

import application.port.in.command.CreateCartCommand;

/**
 * Puerto de entrada para el caso de uso CreateCart.
 */
public interface CreateCartUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(CreateCartCommand command);
}

package application.port.in;

import application.port.in.command.ActivateUserCommand;

/**
 * Puerto de entrada para el caso de uso ActivateUser.
 */
public interface ActivateUserUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(ActivateUserCommand command);
}

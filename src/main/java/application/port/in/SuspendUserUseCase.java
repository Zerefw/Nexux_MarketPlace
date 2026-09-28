package application.port.in;

import application.port.in.command.SuspendUserCommand;

/**
 * Puerto de entrada para el caso de uso SuspendUser.
 */
public interface SuspendUserUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(SuspendUserCommand command);
}

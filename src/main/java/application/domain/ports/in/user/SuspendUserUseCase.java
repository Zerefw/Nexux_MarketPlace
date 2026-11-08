package application.domain.ports.in.user;

import application.domain.ports.in.command.user.SuspendUserCommand;

/**
 * Puerto de entrada para el caso de uso de suspension de usuario.
 */
public interface SuspendUserUseCase {

    /**
     * Suspende preventivamente la cuenta de un usuario.
     *
     * @param command Comando con el identificador del usuario.
     */
    void suspendUser(SuspendUserCommand command);
}

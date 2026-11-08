package application.domain.ports.in.user;

import application.domain.ports.in.command.user.ActivateUserCommand;

/**
 * Puerto de entrada para el caso de uso de reactivacion de usuario.
 */
public interface ActivateUserUseCase {

    /**
     * Reactiva la cuenta de un usuario previamente suspendido.
     *
     * @param command Comando con el identificador del usuario.
     */
    void activateUser(ActivateUserCommand command);
}

package application.usecase;

import application.port.in.ActivateUserUseCase;
import application.port.in.command.ActivateUserCommand;
import application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para reactivar a un usuario previamente suspendido.
 */
@Service
@RequiredArgsConstructor
public class ActivateUserService implements ActivateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Reactiva un usuario en el sistema.
     * @param command Comando con el identificador del usuario a reactivar.
     */
    @Override
    @Transactional
    public void activateUser(ActivateUserCommand command) {
        // Implementacion
    }
}

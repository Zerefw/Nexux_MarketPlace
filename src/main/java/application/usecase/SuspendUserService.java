package application.usecase;

import application.port.in.SuspendUserUseCase;
import application.port.in.command.SuspendUserCommand;
import application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para suspender o bloquear a un usuario.
 */
@Service
@RequiredArgsConstructor
public class SuspendUserService implements SuspendUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Suspende un usuario en el sistema.
     * @param command Comando con el identificador del usuario.
     */
    @Override
    @Transactional
    public void suspendUser(SuspendUserCommand command) {
        // Implementacion
    }
}

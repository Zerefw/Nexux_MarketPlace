package application.domain.services.user;

import application.domain.exception.DomainException;
import application.domain.model.entity.User;
import application.domain.ports.in.command.user.SuspendUserCommand;
import application.domain.ports.in.user.SuspendUserUseCase;
import application.domain.ports.out.user.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para suspender o bloquear preventivamente a un usuario.
 * Implementa el puerto de entrada SuspendUserUseCase.
 */
@Service
@RequiredArgsConstructor
public class SuspendUserService implements SuspendUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Suspende la cuenta de un usuario en el sistema.
     *
     * @param command Comando con el identificador del usuario a suspender.
     */
    @Override
    @Transactional
    public void suspendUser(SuspendUserCommand command) {
        User user = userRepositoryPort.findById(command.userId())
                .orElseThrow(() -> new DomainException("Usuario no encontrado con ID: " + command.userId()));
        user.suspend();
        userRepositoryPort.save(user);
    }
}

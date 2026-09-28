package application.domain.services;

import application.domain.ports.in.SuspendUserUseCase;
import application.domain.ports.in.command.SuspendUserCommand;
import application.domain.ports.out.UserRepositoryPort;
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
        var user = userRepositoryPort.findById(command.userId())
                .orElseThrow(() -> new application.domain.exception.DomainException("Usuario no encontrado"));
        user.suspend();
        userRepositoryPort.save(user);
    }
}

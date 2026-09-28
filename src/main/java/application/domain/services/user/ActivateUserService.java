package application.domain.services;

import application.domain.ports.in.ActivateUserUseCase;
import application.domain.ports.in.command.ActivateUserCommand;
import application.domain.ports.out.UserRepositoryPort;
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
        var user = userRepositoryPort.findById(command.userId())
                .orElseThrow(() -> new application.domain.exception.DomainException("Usuario no encontrado"));
        user.activate();
        userRepositoryPort.save(user);
    }
}

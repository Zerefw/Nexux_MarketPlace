package application.domain.services.user;

import application.domain.exception.DomainException;
import application.domain.model.entity.User;
import application.domain.ports.in.command.user.ActivateUserCommand;
import application.domain.ports.in.user.ActivateUserUseCase;
import application.domain.ports.out.user.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para reactivar a un usuario previamente suspendido.
 * Implementa el puerto de entrada ActivateUserUseCase.
 */
@Service
@RequiredArgsConstructor
public class ActivateUserService implements ActivateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    /**
     * Reactiva la cuenta de un usuario en el sistema validando su identificador.
     *
     * @param command Comando con el identificador del usuario a reactivar.
     */
    @Override
    @Transactional
    public void activateUser(ActivateUserCommand command) {
        if (command.userId() == null || command.userId().isBlank()) {
            throw new DomainException("El identificador del usuario es obligatorio");
        }
        User user = userRepositoryPort.findById(command.userId())
                .orElseThrow(() -> new DomainException("Usuario no encontrado con ID: " + command.userId()));
        user.activate();
        userRepositoryPort.save(user);
    }
}

package application.domain.services.user;

import application.domain.exception.DomainException;
import application.domain.model.entity.BuyerProfile;
import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
import application.domain.ports.in.command.user.RegisterBuyerCommand;
import application.domain.ports.in.user.RegisterBuyerUseCase;
import application.domain.ports.out.user.BuyerProfileRepositoryPort;
import application.domain.ports.out.user.PasswordEncoderPort;
import application.domain.ports.out.user.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion encargado de registrar un nuevo comprador en el sistema.
 * Implementa el puerto de entrada RegisterBuyerUseCase.
 */
@Service
@RequiredArgsConstructor
public class RegisterBuyerService implements RegisterBuyerUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    /**
     * Registra un comprador de manera transaccional validando unicidad de correo y documento.
     *
     * @param command Comando que contiene los datos del comprador a registrar.
     * @return Entidad User creada y persistida.
     */
    @Override
    @Transactional
    public User registerBuyer(RegisterBuyerCommand command) {
        if (command.getEmail() == null || command.getRawPassword() == null || command.getRawPassword().isBlank()) {
            throw new DomainException("El correo electronico y la contrasena son requeridos");
        }
        Email email = new Email(command.getEmail());
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DomainException("El correo electronico ya esta registrado");
        }
        if (userRepositoryPort.existsByIdentificationDocument(command.getIdentificationDocument())) {
            throw new DomainException("El documento de identificacion ya esta registrado");
        }

        String encodedPassword = passwordEncoderPort.encode(command.getRawPassword());
        User user = User.create(
                command.getIdentificationDocument(),
                command.getFullName(),
                email,
                UserRole.BUYER
        );
        User savedUser = userRepositoryPort.save(user, encodedPassword);

        BuyerProfile profile = BuyerProfile.create(
                savedUser.getId(),
                command.getPrimaryAddress()
        );
        buyerProfileRepositoryPort.save(profile);

        return savedUser;
    }
}

package application.domain.services;

import application.domain.ports.in.RegisterBuyerUseCase;
import application.domain.ports.in.command.RegisterBuyerCommand;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.ports.out.BuyerProfileRepositoryPort;
import application.domain.ports.out.PasswordEncoderPort;
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
     * Registra un comprador de manera transaccional.
     * @param command Comando que contiene los datos del comprador a registrar.
     */
    @Override
    @Transactional
    public void registerBuyer(RegisterBuyerCommand command) {
        if (command.getEmail() == null || command.getRawPassword() == null) {
            throw new application.domain.exception.DomainException("Email y password son requeridos");
        }
        var encodedPassword = passwordEncoderPort.encode(command.getRawPassword());
        var user = application.domain.model.entity.User.create(
                command.getIdentificationDocument(),
                command.getFullName(),
                new application.domain.model.valueobject.Email(command.getEmail()),
                application.domain.model.valueobject.UserRole.BUYER
        );
        user = userRepositoryPort.save(user, encodedPassword);
        
        var profile = application.domain.model.entity.BuyerProfile.create(
                user.getId(),
                command.getPrimaryAddress()
        );
        buyerProfileRepositoryPort.save(profile);
    }
}

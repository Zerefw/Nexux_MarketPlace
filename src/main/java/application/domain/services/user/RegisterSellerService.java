package application.domain.services;

import application.domain.ports.in.RegisterSellerUseCase;
import application.domain.ports.in.command.RegisterSellerCommand;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.ports.out.SellerProfileRepositoryPort;
import application.domain.ports.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion encargado de registrar un nuevo vendedor en el sistema.
 * Implementa el puerto de entrada RegisterSellerUseCase.
 */
@Service
@RequiredArgsConstructor
public class RegisterSellerService implements RegisterSellerUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    /**
     * Registra un vendedor de manera transaccional.
     * @param command Comando que contiene los datos del vendedor a registrar.
     */
    @Override
    @Transactional
    public void registerSeller(RegisterSellerCommand command) {
        if (command.getEmail() == null || command.getRawPassword() == null) {
            throw new application.domain.exception.DomainException("Email y password son requeridos");
        }
        var encodedPassword = passwordEncoderPort.encode(command.getRawPassword());
        var user = application.domain.model.entity.User.create(
                command.getIdentificationDocument(),
                command.getFullName(),
                new application.domain.model.valueobject.Email(command.getEmail()),
                application.domain.model.valueobject.UserRole.SELLER
        );
        user = userRepositoryPort.save(user, encodedPassword);
        
        var profile = application.domain.model.entity.SellerProfile.create(
                user.getId(),
                command.getStoreName(),
                command.getTaxId(),
                new application.domain.model.valueobject.Email(command.getEmail())
        );
        sellerProfileRepositoryPort.save(profile);
    }
}

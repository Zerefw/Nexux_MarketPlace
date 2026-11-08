package application.domain.services.user;

import application.domain.exception.DomainException;
import application.domain.model.entity.SellerProfile;
import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
import application.domain.ports.in.command.user.RegisterSellerCommand;
import application.domain.ports.in.user.RegisterSellerUseCase;
import application.domain.ports.out.user.PasswordEncoderPort;
import application.domain.ports.out.user.SellerProfileRepositoryPort;
import application.domain.ports.out.user.UserRepositoryPort;
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
     * Registra un vendedor de manera transaccional validando unicidad de correo y documento.
     *
     * @param command Comando que contiene los datos del vendedor a registrar.
     * @return Entidad User creada y persistida.
     */
    @Override
    @Transactional
    public User registerSeller(RegisterSellerCommand command) {
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
                UserRole.SELLER
        );
        User savedUser = userRepositoryPort.save(user, encodedPassword);

        SellerProfile profile = SellerProfile.create(
                savedUser.getId(),
                command.getStoreName(),
                command.getTaxId(),
                email
        );
        sellerProfileRepositoryPort.save(profile);

        return savedUser;
    }
}

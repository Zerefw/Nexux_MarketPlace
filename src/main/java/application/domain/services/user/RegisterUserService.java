package application.domain.services.user;

import application.domain.exception.DomainException;
import application.domain.model.entity.BuyerProfile;
import application.domain.model.entity.SellerProfile;
import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
import application.domain.ports.in.command.user.RegisterBuyerCommand;
import application.domain.ports.in.command.user.RegisterSellerCommand;
import application.domain.ports.in.user.RegisterUserUseCase;
import application.domain.ports.out.user.BuyerProfileRepositoryPort;
import application.domain.ports.out.user.PasswordEncoderPort;
import application.domain.ports.out.user.SellerProfileRepositoryPort;
import application.domain.ports.out.user.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para el registro integral de compradores y vendedores.
 * Orquesta la validacion de unicidad, el cifrado de credenciales y la creacion de perfiles.
 */
@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;
    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    /**
     * Registra un nuevo comprador en el sistema junto con su perfil comercial.
     *
     * @param command Comando con los datos de registro del comprador.
     * @return Entidad User persistida.
     */
    @Override
    @Transactional
    public User registerBuyer(RegisterBuyerCommand command) {
        Email emailVO = new Email(command.getEmail());
        validateUserUniqueness(emailVO, command.getIdentificationDocument());

        User user = User.create(
                command.getIdentificationDocument(),
                command.getFullName(),
                emailVO,
                UserRole.BUYER
        );

        String encodedPassword = passwordEncoderPort.encode(command.getRawPassword());
        User savedUser = userRepositoryPort.save(user, encodedPassword);

        BuyerProfile profile = BuyerProfile.create(savedUser.getId(), command.getPrimaryAddress());
        buyerProfileRepositoryPort.save(profile);

        return savedUser;
    }

    /**
     * Registra un nuevo vendedor en el sistema junto con el perfil de su tienda.
     *
     * @param command Comando con los datos de registro del vendedor.
     * @return Entidad User persistida.
     */
    @Override
    @Transactional
    public User registerSeller(RegisterSellerCommand command) {
        Email emailVO = new Email(command.getEmail());
        validateUserUniqueness(emailVO, command.getIdentificationDocument());

        User user = User.create(
                command.getIdentificationDocument(),
                command.getFullName(),
                emailVO,
                UserRole.SELLER
        );

        String encodedPassword = passwordEncoderPort.encode(command.getRawPassword());
        User savedUser = userRepositoryPort.save(user, encodedPassword);

        SellerProfile profile = SellerProfile.create(
                savedUser.getId(),
                command.getStoreName(),
                command.getTaxId(),
                emailVO
        );
        sellerProfileRepositoryPort.save(profile);

        return savedUser;
    }

    /**
     * Valida que el correo electronico y el documento de identificacion no esten duplicados.
     *
     * @param email                  Objeto de valor Email a verificar.
     * @param identificationDocument Documento de identidad a verificar.
     */
    private void validateUserUniqueness(Email email, String identificationDocument) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DomainException("El correo electronico ya se encuentra registrado");
        }
        if (userRepositoryPort.existsByIdentificationDocument(identificationDocument)) {
            throw new DomainException("El documento de identificacion ya se encuentra registrado");
        }
    }
}

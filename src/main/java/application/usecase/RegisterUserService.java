package application.usecase;

import application.domain.exception.DomainException;
import application.domain.model.entity.BuyerProfile;
import application.domain.model.entity.SellerProfile;
import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
import application.port.in.RegisterUserUseCase;
import application.port.in.command.RegisterBuyerCommand;
import application.port.in.command.RegisterSellerCommand;
import application.port.out.BuyerProfileRepositoryPort;
import application.port.out.PasswordEncoderPort;
import application.port.out.SellerProfileRepositoryPort;
import application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;
    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

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

    private void validateUserUniqueness(Email email, String identificationDocument) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DomainException("Email is already registered");
        }
        if (userRepositoryPort.existsByIdentificationDocument(identificationDocument)) {
            throw new DomainException("Identification document is already registered");
        }
    }
}

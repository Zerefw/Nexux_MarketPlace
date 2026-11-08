package application.domain.services.user;

import application.domain.exception.DomainException;
import application.domain.model.entity.BuyerProfile;
import application.domain.ports.in.command.user.AddSecondaryAddressCommand;
import application.domain.ports.in.user.AddSecondaryAddressUseCase;
import application.domain.ports.out.user.BuyerProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para agregar una direccion secundaria a un perfil de comprador.
 * Implementa el puerto de entrada AddSecondaryAddressUseCase.
 */
@Service
@RequiredArgsConstructor
public class AddSecondaryAddressService implements AddSecondaryAddressUseCase {

    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;

    /**
     * Agrega una direccion secundaria de entrega al comprador.
     *
     * @param command Comando con el identificador del comprador y la direccion secundaria.
     */
    @Override
    @Transactional
    public void addSecondaryAddress(AddSecondaryAddressCommand command) {
        BuyerProfile profile = buyerProfileRepositoryPort.findByBuyerId(command.buyerId())
                .orElseThrow(() -> new DomainException("Perfil de comprador no encontrado para el usuario: " + command.buyerId()));
        profile.addSecondaryAddress(command.address());
        buyerProfileRepositoryPort.save(profile);
    }
}

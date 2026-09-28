package application.domain.services;

import application.domain.ports.in.AddSecondaryAddressUseCase;
import application.domain.ports.in.command.AddSecondaryAddressCommand;
import application.domain.ports.out.BuyerProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para agregar una direccion secundaria a un perfil de comprador.
 */
@Service
@RequiredArgsConstructor
public class AddSecondaryAddressService implements AddSecondaryAddressUseCase {

    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;

    /**
     * Agrega una direccion secundaria.
     * @param command Comando con los datos de la direccion y el comprador.
     */
    @Override
    @Transactional
    public void addSecondaryAddress(AddSecondaryAddressCommand command) {
        var profile = buyerProfileRepositoryPort.findById(command.buyerId())
                .orElseThrow(() -> new application.domain.exception.DomainException("Perfil de comprador no encontrado"));
        profile.addSecondaryAddress(command.address());
        buyerProfileRepositoryPort.save(profile);
    }
}

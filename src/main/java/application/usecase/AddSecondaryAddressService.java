package application.usecase;

import application.port.in.AddSecondaryAddressUseCase;
import application.port.in.command.AddSecondaryAddressCommand;
import application.port.out.BuyerProfileRepositoryPort;
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
        // Implementacion
    }
}

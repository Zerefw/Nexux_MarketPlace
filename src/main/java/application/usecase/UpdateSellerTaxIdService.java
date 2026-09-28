package application.usecase;

import application.port.in.UpdateSellerTaxIdUseCase;
import application.port.in.command.UpdateSellerTaxIdCommand;
import application.port.out.SellerProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para actualizar la informacion fiscal de un vendedor.
 */
@Service
@RequiredArgsConstructor
public class UpdateSellerTaxIdService implements UpdateSellerTaxIdUseCase {

    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;

    /**
     * Actualiza el identificador fiscal de un vendedor.
     * @param command Comando con los datos fiscales nuevos.
     */
    @Override
    @Transactional
    public void updateSellerTaxId(UpdateSellerTaxIdCommand command) {
        // Implementacion
    }
}

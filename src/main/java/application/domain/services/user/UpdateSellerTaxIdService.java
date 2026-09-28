package application.domain.services;

import application.domain.ports.in.UpdateSellerTaxIdUseCase;
import application.domain.ports.in.command.UpdateSellerTaxIdCommand;
import application.domain.ports.out.SellerProfileRepositoryPort;
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
        var profile = sellerProfileRepositoryPort.findById(command.sellerId())
                .orElseThrow(() -> new application.domain.exception.DomainException("Perfil de vendedor no encontrado"));
        profile.updateTaxId(command.taxId());
        sellerProfileRepositoryPort.save(profile);
    }
}

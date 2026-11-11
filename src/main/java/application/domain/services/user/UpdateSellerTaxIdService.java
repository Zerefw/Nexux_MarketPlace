package application.domain.services.user;

import application.domain.exception.DomainException;
import application.domain.model.entity.SellerProfile;
import application.domain.ports.in.command.user.UpdateSellerTaxIdCommand;
import application.domain.ports.in.user.UpdateSellerTaxIdUseCase;
import application.domain.ports.out.user.SellerProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para actualizar la informacion fiscal de un vendedor.
 * Implementa el puerto de entrada UpdateSellerTaxIdUseCase.
 */
@Service
@RequiredArgsConstructor
public class UpdateSellerTaxIdService implements UpdateSellerTaxIdUseCase {

    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;

    /**
     * Actualiza el identificador fiscal en el perfil del vendedor validando sus datos.
     *
     * @param command Comando con el identificador del vendedor y el nuevo identificador fiscal.
     */
    @Override
    @Transactional
    public void updateSellerTaxId(UpdateSellerTaxIdCommand command) {
        if (command.sellerId() == null || command.sellerId().isBlank()) {
            throw new DomainException("El identificador del vendedor es obligatorio");
        }
        SellerProfile profile = sellerProfileRepositoryPort.findBySellerId(command.sellerId())
                .orElseThrow(() -> new DomainException("Perfil de vendedor no encontrado para el usuario: " + command.sellerId()));
        profile.updateTaxId(command.taxId());
        sellerProfileRepositoryPort.save(profile);
    }
}

package application.domain.services;

import application.domain.ports.in.AddProductVariantUseCase;
import application.domain.ports.in.command.AddProductVariantCommand;
import application.domain.ports.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para aÃ±adir nuevas variantes a un producto existente.
 */
@Service
@RequiredArgsConstructor
public class AddProductVariantService implements AddProductVariantUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * AÃ±ade una variante a un producto.
     * @param command Comando con los datos de la variante.
     */
    @Override
    @Transactional
    public void addProductVariant(AddProductVariantCommand command) {
        var sku = new application.domain.model.valueobject.Sku(command.sku());
        var product = productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new application.domain.exception.DomainException("Producto no encontrado"));
        product.addVariant(command.variant());
        productRepositoryPort.save(product);
    }
}

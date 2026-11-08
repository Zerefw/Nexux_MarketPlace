package application.domain.services.catalog;

import application.domain.exception.DomainException;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.catalog.AddProductVariantUseCase;
import application.domain.ports.in.command.catalog.AddProductVariantCommand;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para anadir nuevas variantes comerciales a un producto existente.
 * Implementa el caso de uso AddProductVariantUseCase.
 */
@Service
@RequiredArgsConstructor
public class AddProductVariantService implements AddProductVariantUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Agrega una variante a un producto del catalogo y persiste los cambios.
     *
     * @param command Comando con el SKU del producto y la variante a registrar.
     * @return Entidad Product actualizada.
     */
    @Override
    @Transactional
    public Product addProductVariant(AddProductVariantCommand command) {
        Sku sku = new Sku(command.sku());
        Product product = productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new DomainException("Producto no encontrado con SKU: " + command.sku()));
        product.addVariant(command.variant());
        return productRepositoryPort.save(product);
    }
}

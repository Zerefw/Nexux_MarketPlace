package application.domain.services.catalog;

import application.domain.exception.DomainException;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.catalog.DeactivateProductUseCase;
import application.domain.ports.in.command.catalog.DeactivateProductCommand;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para realizar el borrado logico de un producto en el catalogo.
 * Implementa el caso de uso DeactivateProductUseCase.
 */
@Service
@RequiredArgsConstructor
public class DeactivateProductService implements DeactivateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Desactiva un producto en el catalogo buscando por su SKU.
     *
     * @param command Comando con el SKU del producto a desactivar.
     */
    @Override
    @Transactional
    public void deactivateProduct(DeactivateProductCommand command) {
        Sku sku = new Sku(command.sku());
        Product product = productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new DomainException("Producto no encontrado con SKU: " + command.sku()));
        product.deactivate();
        productRepositoryPort.save(product);
    }
}

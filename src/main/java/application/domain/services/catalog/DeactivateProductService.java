package application.domain.services;

import application.domain.ports.in.DeactivateProductUseCase;
import application.domain.ports.in.command.DeactivateProductCommand;
import application.domain.ports.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para realizar un borrado logico de un producto.
 */
@Service
@RequiredArgsConstructor
public class DeactivateProductService implements DeactivateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Desactiva un producto en el catalogo.
     * @param command Comando con la identificacion del producto.
     */
    @Override
    @Transactional
    public void deactivateProduct(DeactivateProductCommand command) {
        var sku = new application.domain.model.valueobject.Sku(command.sku());
        var product = productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new application.domain.exception.DomainException("Producto no encontrado"));
        product.deactivate();
        productRepositoryPort.save(product);
    }
}

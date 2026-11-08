package application.domain.services.catalog;

import application.domain.exception.DomainException;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.catalog.UpdateProductUseCase;
import application.domain.ports.in.command.catalog.UpdateProductCommand;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para modificar los atributos descriptivos y el precio de un producto.
 * Implementa el caso de uso UpdateProductUseCase.
 */
@Service
@RequiredArgsConstructor
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Actualiza la descripcion y el precio unitario de un producto identificado por su SKU.
     *
     * @param command Comando con los nuevos datos del producto.
     * @return Entidad Product actualizada.
     */
    @Override
    @Transactional
    public Product updateProduct(UpdateProductCommand command) {
        Sku sku = new Sku(command.sku());
        Product product = productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new DomainException("Producto no encontrado con SKU: " + command.sku()));
        Money newPrice = new Money(command.priceAmount(), command.priceCurrency());
        product.updateDetails(command.description(), newPrice);
        return productRepositoryPort.save(product);
    }
}

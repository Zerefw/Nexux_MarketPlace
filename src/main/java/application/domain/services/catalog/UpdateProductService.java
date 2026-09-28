package application.domain.services;

import application.domain.ports.in.UpdateProductUseCase;
import application.domain.ports.in.command.UpdateProductCommand;
import application.domain.ports.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para modificar atributos base de un producto.
 */
@Service
@RequiredArgsConstructor
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Modifica los datos de un producto.
     * @param command Comando con los nuevos datos del producto.
     */
    @Override
    @Transactional
    public void updateProduct(UpdateProductCommand command) {
        var sku = new application.domain.model.valueobject.Sku(command.sku());
        var product = productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new application.domain.exception.DomainException("Producto no encontrado"));
        var newPrice = new application.domain.model.valueobject.Money(command.priceAmount(), command.priceCurrency());
        product.updateDetails(command.description(), newPrice);
        productRepositoryPort.save(product);
    }
}

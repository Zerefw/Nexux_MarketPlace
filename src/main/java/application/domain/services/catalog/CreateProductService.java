package application.domain.services;

import application.domain.ports.in.CreateProductUseCase;
import application.domain.ports.in.command.CreateProductCommand;
import application.domain.ports.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para crear un nuevo producto en el catalogo.
 */
@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Crea un producto base en el catalogo.
     * @param command Comando con los datos de creacion del producto.
     */
    @Override
    @Transactional
    public void createProduct(CreateProductCommand command) {
        var sku = new application.domain.model.valueobject.Sku(command.getSku());
        if (productRepositoryPort.existsBySku(sku)) {
            throw new application.domain.exception.DomainException("El producto ya existe");
        }
        var price = new application.domain.model.valueobject.Money(command.getPriceAmount(), command.getPriceCurrency());
        var type = application.domain.model.valueobject.ProductType.valueOf(command.getType());
        var product = application.domain.model.entity.Product.create(sku, command.getSellerId(), command.getName(), price, type);
        if (command.getVariants() != null) {
            command.getVariants().forEach(product::addVariant);
        }
        productRepositoryPort.save(product);
    }
}

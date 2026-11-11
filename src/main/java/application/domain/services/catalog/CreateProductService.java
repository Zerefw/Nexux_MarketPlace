package application.domain.services.catalog;

import application.domain.exception.DomainException;
import application.domain.model.entity.Product;
import application.domain.model.entity.SellerProfile;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.ProductType;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.catalog.CreateProductUseCase;
import application.domain.ports.in.command.catalog.CreateProductCommand;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import application.domain.ports.out.user.SellerProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para crear un nuevo producto en el catalogo.
 * Implementa el caso de uso CreateProductUseCase.
 */
@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;

    /**
     * Crea un producto base en el catalogo validando que el vendedor exista y este activo,
     * y que el SKU sea unico.
     *
     * @param command Comando con los datos de creacion del producto.
     * @return Entidad Product creada y persistida.
     */
    @Override
    @Transactional
    public Product createProduct(CreateProductCommand command) {
        if (command.getSellerId() == null || command.getSellerId().isBlank()) {
            throw new DomainException("El identificador del vendedor (sellerId) es obligatorio");
        }
        SellerProfile seller = sellerProfileRepositoryPort.findBySellerId(command.getSellerId())
                .orElseThrow(() -> new DomainException("No existe un perfil de vendedor registrado con ID: " + command.getSellerId()));
        if (!seller.isActive()) {
            throw new DomainException("El perfil del vendedor se encuentra inactivo");
        }

        Sku sku = new Sku(command.getSku());
        if (productRepositoryPort.existsBySku(sku)) {
            throw new DomainException("El producto ya existe con el SKU: " + sku.getCode());
        }
        if (command.getType() == null || command.getType().isBlank()) {
            throw new DomainException("El tipo de producto (PHYSICAL o DIGITAL) es obligatorio");
        }

        Money price = new Money(command.getPriceAmount(), command.getPriceCurrency());
        ProductType type = ProductType.valueOf(command.getType().toUpperCase());
        Product product = Product.create(sku, command.getSellerId(), command.getName(), price, type);

        if (command.getDescription() != null && !command.getDescription().isBlank()) {
            product.updateDetails(command.getDescription(), price);
        }

        if (command.getVariants() != null) {
            command.getVariants().forEach(product::addVariant);
        }
        return productRepositoryPort.save(product);
    }
}

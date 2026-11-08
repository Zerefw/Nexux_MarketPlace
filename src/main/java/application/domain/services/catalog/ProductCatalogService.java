package application.domain.services.catalog;

import application.domain.exception.DomainException;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.ProductType;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.catalog.ManageProductUseCase;
import application.domain.ports.in.command.catalog.CreateProductCommand;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para gestionar el ciclo de vida general de los productos del catalogo.
 * Implementa el puerto de entrada ManageProductUseCase.
 */
@Service
@RequiredArgsConstructor
public class ProductCatalogService implements ManageProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Crea un nuevo producto en el catalogo validando la unicidad de su SKU.
     *
     * @param command Comando con los datos de creacion del producto.
     * @return Entidad Product persistida.
     */
    @Override
    @Transactional
    public Product createProduct(CreateProductCommand command) {
        Sku skuVO = new Sku(command.getSku());

        if (productRepositoryPort.existsBySku(skuVO)) {
            throw new DomainException("El SKU ya se encuentra registrado en el catalogo: " + skuVO.getCode());
        }

        Money priceVO = new Money(command.getPriceAmount(), command.getPriceCurrency());
        ProductType typeEnum = ProductType.valueOf(command.getType().toUpperCase());

        Product product = Product.create(
                skuVO,
                command.getSellerId(),
                command.getName(),
                priceVO,
                typeEnum
        );

        if (command.getDescription() != null && !command.getDescription().isBlank()) {
            product.updateDetails(command.getDescription(), priceVO);
        }

        if (command.getVariants() != null) {
            for (String variant : command.getVariants()) {
                product.addVariant(variant);
            }
        }

        return productRepositoryPort.save(product);
    }

    /**
     * Desactiva logicamente un producto existente mediante su identificador.
     *
     * @param productId Identificador unico del producto a desactivar.
     */
    @Override
    @Transactional
    public void deactivateProduct(String productId) {
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new DomainException("Producto no encontrado con ID: " + productId));
        product.deactivate();
        productRepositoryPort.save(product);
    }
}

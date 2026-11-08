package application.domain.services.catalog;

import application.domain.exception.DomainException;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.catalog.FindProductBySkuUseCase;
import application.domain.ports.in.command.catalog.FindProductBySkuCommand;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para consultar el detalle de un producto por su SKU.
 * Implementa el caso de uso FindProductBySkuUseCase.
 */
@Service
@RequiredArgsConstructor
public class FindProductBySkuService implements FindProductBySkuUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Busca y retorna un producto basado en su SKU unico.
     *
     * @param command Comando que incluye el SKU de busqueda.
     * @return Entidad Product encontrada.
     */
    @Override
    @Transactional(readOnly = true)
    public Product findProductBySku(FindProductBySkuCommand command) {
        Sku sku = new Sku(command.sku());
        return productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new DomainException("Producto no encontrado con SKU: " + command.sku()));
    }
}

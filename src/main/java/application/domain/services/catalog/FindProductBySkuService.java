package application.domain.services;

import application.domain.ports.in.FindProductBySkuUseCase;
import application.domain.ports.in.command.FindProductBySkuCommand;
import application.domain.ports.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para consultar el detalle de un producto por su SKU.
 */
@Service
@RequiredArgsConstructor
public class FindProductBySkuService implements FindProductBySkuUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Busca y retorna un producto basado en su SKU.
     * @param command Comando que incluye el SKU de busqueda.
     */
    @Override
    @Transactional(readOnly = true)
    public void findProductBySku(FindProductBySkuCommand command) {
        var sku = new application.domain.model.valueobject.Sku(command.sku());
        productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new application.domain.exception.DomainException("Producto no encontrado"));
    }
}

package application.usecase;

import application.port.in.FindProductBySkuUseCase;
import application.port.in.command.FindProductBySkuCommand;
import application.port.out.ProductRepositoryPort;
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
        // Implementacion
    }
}

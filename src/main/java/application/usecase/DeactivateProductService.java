package application.usecase;

import application.port.in.DeactivateProductUseCase;
import application.port.in.command.DeactivateProductCommand;
import application.port.out.ProductRepositoryPort;
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
        // Implementacion
    }
}

package application.usecase;

import application.port.in.UpdateProductUseCase;
import application.port.in.command.UpdateProductCommand;
import application.port.out.ProductRepositoryPort;
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
        // Implementacion
    }
}

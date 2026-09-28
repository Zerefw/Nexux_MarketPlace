package application.usecase;

import application.port.in.CreateProductUseCase;
import application.port.in.command.CreateProductCommand;
import application.port.out.ProductRepositoryPort;
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
        // Implementacion
    }
}

package application.usecase;

import application.port.in.ListActiveProductsUseCase;
import application.port.in.command.ListActiveProductsCommand;
import application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para listar y retornar los productos activos.
 */
@Service
@RequiredArgsConstructor
public class ListActiveProductsService implements ListActiveProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Retorna una lista de productos activos.
     * @param command Comando con posibles filtros.
     */
    @Override
    @Transactional(readOnly = true)
    public void listActiveProducts(ListActiveProductsCommand command) {
        // Implementacion
    }
}

package application.domain.services.catalog;

import application.domain.model.entity.Product;
import application.domain.ports.in.catalog.ListActiveProductsUseCase;
import application.domain.ports.in.command.catalog.ListActiveProductsCommand;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicacion para listar todos los productos activos del catalogo.
 * Implementa el caso de uso ListActiveProductsUseCase.
 */
@Service
@RequiredArgsConstructor
public class ListActiveProductsService implements ListActiveProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Recupera y retorna la lista de productos que se encuentran activos.
     *
     * @param command Comando de consulta.
     * @return Lista de entidades Product activas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> listActiveProducts(ListActiveProductsCommand command) {
        return productRepositoryPort.findAllActive();
    }
}

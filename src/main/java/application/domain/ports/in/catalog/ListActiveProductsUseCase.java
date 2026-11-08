package application.domain.ports.in.catalog;

import application.domain.model.entity.Product;
import application.domain.ports.in.command.catalog.ListActiveProductsCommand;

import java.util.List;

/**
 * Puerto de entrada para el caso de uso de listado de productos activos.
 */
public interface ListActiveProductsUseCase {

    /**
     * Retorna todos los productos activos disponibles en el catalogo.
     *
     * @param command Comando de consulta.
     * @return Lista de productos activos.
     */
    List<Product> listActiveProducts(ListActiveProductsCommand command);
}

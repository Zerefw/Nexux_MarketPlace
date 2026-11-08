package application.domain.ports.in.catalog;

import application.domain.model.entity.Product;
import application.domain.ports.in.command.catalog.FindProductBySkuCommand;

/**
 * Puerto de entrada para el caso de uso de busqueda de producto por SKU.
 */
public interface FindProductBySkuUseCase {

    /**
     * Consulta un producto por su codigo SKU.
     *
     * @param command Comando con el SKU a buscar.
     * @return Entidad Product encontrada.
     */
    Product findProductBySku(FindProductBySkuCommand command);
}

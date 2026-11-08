package application.domain.ports.in.catalog;

import application.domain.model.entity.Product;
import application.domain.ports.in.command.catalog.CreateProductCommand;

/**
 * Puerto de entrada para el caso de uso de creacion de producto.
 */
public interface CreateProductUseCase {

    /**
     * Crea un producto nuevo en el catalogo.
     *
     * @param command Comando con los datos del producto.
     * @return Entidad Product creada.
     */
    Product createProduct(CreateProductCommand command);
}

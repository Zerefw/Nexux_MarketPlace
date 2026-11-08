package application.domain.ports.in.catalog;

import application.domain.model.entity.Product;
import application.domain.ports.in.command.catalog.CreateProductCommand;

/**
 * Puerto de entrada general para administrar operaciones del catalogo de productos.
 */
public interface ManageProductUseCase {

    /**
     * Registra un nuevo producto en el catalogo.
     *
     * @param command Comando con los datos de creacion.
     * @return Producto creado y persistido.
     */
    Product createProduct(CreateProductCommand command);

    /**
     * Desactiva logicamente un producto por su identificador unico.
     *
     * @param productId Identificador del producto.
     */
    void deactivateProduct(String productId);
}

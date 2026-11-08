package application.domain.ports.in.catalog;

import application.domain.ports.in.command.catalog.DeactivateProductCommand;

/**
 * Puerto de entrada para el caso de uso de desactivacion logica de un producto.
 */
public interface DeactivateProductUseCase {

    /**
     * Desactiva un producto del catalogo.
     *
     * @param command Comando con el SKU del producto a desactivar.
     */
    void deactivateProduct(DeactivateProductCommand command);
}

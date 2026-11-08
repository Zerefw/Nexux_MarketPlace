package application.domain.ports.in.catalog;

import application.domain.model.entity.Product;
import application.domain.ports.in.command.catalog.UpdateProductCommand;

/**
 * Puerto de entrada para el caso de uso de actualizacion de producto.
 */
public interface UpdateProductUseCase {

    /**
     * Actualiza la descripcion y el precio de un producto existente.
     *
     * @param command Comando con los datos actualizados.
     * @return Entidad Product actualizada.
     */
    Product updateProduct(UpdateProductCommand command);
}

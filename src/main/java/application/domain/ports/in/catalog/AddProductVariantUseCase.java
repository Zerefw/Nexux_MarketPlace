package application.domain.ports.in.catalog;

import application.domain.model.entity.Product;
import application.domain.ports.in.command.catalog.AddProductVariantCommand;

/**
 * Puerto de entrada para el caso de uso de adicion de variantes a un producto.
 */
public interface AddProductVariantUseCase {

    /**
     * Agrega una variante comercial a un producto existente.
     *
     * @param command Comando con el SKU y la variante a agregar.
     * @return Entidad Product actualizada.
     */
    Product addProductVariant(AddProductVariantCommand command);
}

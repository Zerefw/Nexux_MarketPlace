package application.domain.ports.in.order;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.AddItemToCartCommand;

/**
 * Puerto de entrada para el caso de uso de adicion de items al carrito.
 */
public interface AddItemToCartUseCase {

    /**
     * Agrega un item al carrito de compras y recalcula el total de la orden.
     *
     * @param command Comando con los datos del item a agregar.
     * @return Agregado Order actualizado.
     */
    Order execute(AddItemToCartCommand command);
}

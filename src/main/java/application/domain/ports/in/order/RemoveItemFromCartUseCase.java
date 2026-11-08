package application.domain.ports.in.order;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.RemoveItemFromCartCommand;

/**
 * Puerto de entrada para el caso de uso de eliminacion de items del carrito.
 */
public interface RemoveItemFromCartUseCase {

    /**
     * Remueve un producto del carrito de compras y recalcula el total.
     *
     * @param command Comando con el identificador de la orden y el SKU a eliminar.
     * @return Agregado Order actualizado.
     */
    Order execute(RemoveItemFromCartCommand command);
}

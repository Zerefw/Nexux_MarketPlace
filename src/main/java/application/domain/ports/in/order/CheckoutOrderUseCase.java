package application.domain.ports.in.order;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.CheckoutOrderCommand;

/**
 * Puerto de entrada para el caso de uso de checkout de una orden.
 */
public interface CheckoutOrderUseCase {

    /**
     * Procesa el checkout del carrito reservando el inventario requerido y cambiando el estado a PENDING_PAYMENT.
     *
     * @param command Comando con el identificador de la orden y la direccion de envio.
     * @return Agregado Order en estado PENDING_PAYMENT.
     */
    Order execute(CheckoutOrderCommand command);
}

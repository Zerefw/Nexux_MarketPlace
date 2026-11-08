package application.domain.ports.in.order;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.CancelOrderCommand;

/**
 * Puerto de entrada para el caso de uso de cancelacion de orden.
 */
public interface CancelOrderUseCase {

    /**
     * Cancela una orden no despachada y libera cualquier reserva de inventario asociada.
     *
     * @param command Comando con el identificador de la orden y el motivo de cancelacion.
     * @return Agregado Order en estado CANCELLED.
     */
    Order execute(CancelOrderCommand command);
}

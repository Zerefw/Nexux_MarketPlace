package application.domain.ports.in.logistics;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.logistics.DeliverOrderCommand;

/**
 * Puerto de entrada para el caso de uso de confirmacion de entrega de una orden.
 */
public interface DeliverOrderUseCase {

    /**
     * Marca una orden despachada como entregada y finalizada.
     *
     * @param command Comando con el identificador de la orden y el receptor.
     * @return Agregado Order en estado DELIVERED_FINALIZED.
     */
    Order execute(DeliverOrderCommand command);
}

package application.domain.ports.in.order;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.MarkOrderAsPaidCommand;

/**
 * Puerto de entrada para el caso de uso de confirmacion de pago de una orden.
 */
public interface MarkOrderAsPaidUseCase {

    /**
     * Marca una orden como pagada y deduce en firme el inventario previamente reservado.
     *
     * @param command Comando con el identificador de la orden y la referencia de pago.
     * @return Agregado Order en estado PAID.
     */
    Order execute(MarkOrderAsPaidCommand command);
}

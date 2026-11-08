package application.domain.ports.in.logistics;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.logistics.ShipOrderCommand;

/**
 * Puerto de entrada para el caso de uso de despacho fisico de una orden.
 */
public interface ShipOrderUseCase {

    /**
     * Marca una orden pagada como despachada fisicamente.
     *
     * @param command Comando con el identificador de la orden y los datos de guia.
     * @return Agregado Order en estado SHIPPED.
     */
    Order execute(ShipOrderCommand command);
}

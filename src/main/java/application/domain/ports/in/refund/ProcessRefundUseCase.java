package application.domain.ports.in.refund;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.refund.ProcessRefundCommand;

/**
 * Puerto de entrada para el caso de uso de procesamiento de reembolsos y devoluciones.
 */
public interface ProcessRefundUseCase {

    /**
     * Procesa el reembolso de una orden y reintegra las unidades fisicas al inventario.
     *
     * @param command Comando con el identificador de la orden y el motivo del reembolso.
     * @return Agregado Order en estado REFUNDED.
     */
    Order execute(ProcessRefundCommand command);
}

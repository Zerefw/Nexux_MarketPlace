package application.domain.ports.in.command.refund;

/**
 * Comando para solicitar y procesar el reembolso de una orden junto con el reintegro de stock.
 *
 * @param orderId Identificador de la orden a reembolsar.
 * @param reason  Motivo de la devolucion o reembolso.
 */
public record ProcessRefundCommand(String orderId, String reason) {
}

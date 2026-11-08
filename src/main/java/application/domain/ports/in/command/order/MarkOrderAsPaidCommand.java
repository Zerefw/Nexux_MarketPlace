package application.domain.ports.in.command.order;

/**
 * Comando para confirmar el pago de una orden y deducir definitivamente el stock reservado.
 *
 * @param orderId          Identificador de la orden en estado PENDING_PAYMENT.
 * @param paymentReference Referencia o comprobante de la transaccion de pago.
 */
public record MarkOrderAsPaidCommand(String orderId, String paymentReference) {
}

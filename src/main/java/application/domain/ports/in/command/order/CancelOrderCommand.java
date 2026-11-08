package application.domain.ports.in.command.order;

/**
 * Comando para cancelar una orden antes de su despacho y liberar reservas activas.
 *
 * @param orderId Identificador de la orden a cancelar.
 * @param reason  Motivo de la cancelacion.
 */
public record CancelOrderCommand(String orderId, String reason) {
}

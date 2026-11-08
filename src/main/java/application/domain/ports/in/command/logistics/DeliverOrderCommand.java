package application.domain.ports.in.command.logistics;

/**
 * Comando para confirmar la entrega final de una orden al comprador.
 *
 * @param orderId    Identificador de la orden despachada.
 * @param receivedBy Nombre o identificacion de quien recibio el paquete.
 */
public record DeliverOrderCommand(String orderId, String receivedBy) {
}

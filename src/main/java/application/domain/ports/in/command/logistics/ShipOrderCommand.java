package application.domain.ports.in.command.logistics;

/**
 * Comando para registrar el despacho fisico de una orden pagada.
 *
 * @param orderId        Identificador de la orden a despachar.
 * @param trackingNumber Numero de guia o seguimiento logistico.
 * @param carrierName    Nombre de la transportadora encargada.
 */
public record ShipOrderCommand(String orderId, String trackingNumber, String carrierName) {
}

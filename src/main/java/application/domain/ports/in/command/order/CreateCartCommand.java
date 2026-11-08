package application.domain.ports.in.command.order;

/**
 * Comando para inicializar un nuevo carrito de compras para un comprador.
 *
 * @param buyerId  Identificador unico del comprador.
 * @param currency Codigo ISO de la moneda del carrito.
 */
public record CreateCartCommand(String buyerId, String currency) {
}

package application.domain.ports.in.command.order;

/**
 * Comando para ejecutar el proceso de checkout de un carrito, reservando stock
 * y fijando la direccion de entrega.
 *
 * @param orderId         Identificador de la orden en estado CART.
 * @param shippingAddress Direccion fisica de entrega para el envio.
 */
public record CheckoutOrderCommand(String orderId, String shippingAddress) {
}

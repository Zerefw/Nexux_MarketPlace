package application.domain.ports.in.command.order;

/**
 * Comando para eliminar un producto especifico de un carrito de compras.
 *
 * @param orderId Identificador de la orden en estado CART.
 * @param sku     Codigo SKU del producto a remover.
 */
public record RemoveItemFromCartCommand(String orderId, String sku) {
}

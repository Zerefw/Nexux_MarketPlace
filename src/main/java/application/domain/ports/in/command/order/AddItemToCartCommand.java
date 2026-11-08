package application.domain.ports.in.command.order;

import java.math.BigDecimal;

/**
 * Comando para agregar una linea de producto a un carrito de compras existente.
 *
 * @param orderId         Identificador de la orden en estado CART.
 * @param sku             Codigo SKU del producto a agregar.
 * @param quantity        Cantidad de unidades solicitadas.
 * @param unitPriceAmount Valor unitario del producto.
 * @param currency        Codigo ISO de la moneda.
 */
public record AddItemToCartCommand(
        String orderId,
        String sku,
        int quantity,
        BigDecimal unitPriceAmount,
        String currency
) {
}

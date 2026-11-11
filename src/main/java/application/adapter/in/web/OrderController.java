package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.*;
import application.domain.ports.in.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestion de carritos de compra, checkout, pago y cancelacion de ordenes.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateCartUseCase createCartUseCase;
    private final AddItemToCartUseCase addItemToCartUseCase;
    private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
    private final CheckoutOrderUseCase checkoutOrderUseCase;
    private final MarkOrderAsPaidUseCase markOrderAsPaidUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    /**
     * Crea un nuevo carrito de compras vacio para un comprador.
     */
    @PostMapping("/cart")
    public ResponseEntity<ApiResponse<Order>> createCart(@RequestBody CreateCartCommand command) {
        Order cart = createCartUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Carrito de compras creado exitosamente", cart));
    }

    /**
     * Agrega una linea de producto al carrito de compras.
     */
    @PostMapping("/cart/items")
    public ResponseEntity<ApiResponse<Order>> addItemToCart(@RequestBody AddItemToCartCommand command) {
        Order updatedOrder = addItemToCartUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Item agregado al carrito exitosamente", updatedOrder));
    }

    /**
     * Elimina un producto del carrito de compras.
     */
    @DeleteMapping("/{orderId}/items/{sku}")
    public ResponseEntity<ApiResponse<Order>> removeItemFromCart(
            @PathVariable String orderId,
            @PathVariable String sku
    ) {
        Order updatedOrder = removeItemFromCartUseCase.execute(new RemoveItemFromCartCommand(orderId, sku));
        return ResponseEntity.ok(ApiResponse.ok("Item eliminado del carrito exitosamente", updatedOrder));
    }

    /**
     * Ejecuta el checkout del carrito reservando stock y pasando a estado PENDING_PAYMENT.
     */
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Order>> checkoutOrder(@RequestBody CheckoutOrderCommand command) {
        Order checkedOutOrder = checkoutOrderUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Checkout realizado y stock reservado exitosamente", checkedOutOrder));
    }

    /**
     * Confirma el pago de la orden y deduce en firme el inventario reservado.
     */
    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<Order>> markOrderAsPaid(@RequestBody MarkOrderAsPaidCommand command) {
        Order paidOrder = markOrderAsPaidUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Pago confirmado y stock deducido exitosamente", paidOrder));
    }

    /**
     * Cancela una orden antes de su despacho y libera reservas activas.
     */
    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@RequestBody CancelOrderCommand command) {
        Order cancelledOrder = cancelOrderUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Orden cancelada exitosamente", cancelledOrder));
    }
}

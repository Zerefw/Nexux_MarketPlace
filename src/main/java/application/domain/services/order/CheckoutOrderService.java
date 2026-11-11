package application.domain.services.order;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import application.domain.ports.in.command.order.CheckoutOrderCommand;
import application.domain.ports.in.order.CheckoutOrderUseCase;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import application.domain.ports.out.order.OrderRepositoryPort;
import application.domain.services.InventoryReservationDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de aplicacion para transicionar una orden de CART a PENDING_PAYMENT, reservando inventario.
 * Implementa el caso de uso CheckoutOrderUseCase.
 */
@Service
@RequiredArgsConstructor
public class CheckoutOrderService implements CheckoutOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryReservationDomainService inventoryReservationDomainService;

    /**
     * Fija la direccion de envio del carrito, consulta el inventario disponible para todos sus items,
     * delega la reserva al servicio de dominio y confirma el paso a PENDING_PAYMENT.
     *
     * @param command Comando con el identificador de la orden y la direccion de envio.
     * @return Agregado Order en estado PENDING_PAYMENT.
     */
    @Override
    @Transactional
    public Order execute(CheckoutOrderCommand command) {
        if (command.orderId() == null || command.orderId().isBlank()) {
            throw new DomainException("El identificador de la orden es obligatorio");
        }
        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        if (command.shippingAddress() != null && !command.shippingAddress().isBlank()) {
            order.setShippingAddress(command.shippingAddress());
        }

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            inventoryItems.addAll(inventoryRepositoryPort.findBySku(item.getSku()));
        }

        inventoryReservationDomainService.reserveItemsForOrder(order, inventoryItems);

        for (InventoryItem updatedItem : inventoryItems) {
            inventoryRepositoryPort.save(updatedItem);
        }

        order.checkout();
        return orderRepositoryPort.save(order);
    }
}

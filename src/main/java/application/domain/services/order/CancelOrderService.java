package application.domain.services.order;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import application.domain.model.valueobject.OrderStatus;
import application.domain.ports.in.command.order.CancelOrderCommand;
import application.domain.ports.in.order.CancelOrderUseCase;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import application.domain.ports.out.order.OrderRepositoryPort;
import application.domain.services.InventoryReleaseDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de aplicacion para cancelar una orden no despachada, liberando inventario si es necesario.
 * Implementa el caso de uso CancelOrderUseCase.
 */
@Service
@RequiredArgsConstructor
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryReleaseDomainService inventoryReleaseDomainService;

    /**
     * Cancela una orden activa. Si la orden ya habia reservado inventario en estado PENDING_PAYMENT,
     * libera dichas reservas mediante el servicio de dominio antes de persistir la cancelacion.
     *
     * @param command Comando con el identificador de la orden y el motivo de cancelacion.
     * @return Agregado Order en estado CANCELLED.
     */
    @Override
    @Transactional
    public Order execute(CancelOrderCommand command) {
        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        if (order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            List<InventoryItem> inventoryItems = new ArrayList<>();
            for (OrderItem item : order.getItems()) {
                inventoryItems.addAll(inventoryRepositoryPort.findBySku(item.getSku()));
            }

            inventoryReleaseDomainService.releaseReservedItemsForOrder(order, inventoryItems);

            for (InventoryItem updatedItem : inventoryItems) {
                inventoryRepositoryPort.save(updatedItem);
            }
        }

        order.cancel();
        return orderRepositoryPort.save(order);
    }
}

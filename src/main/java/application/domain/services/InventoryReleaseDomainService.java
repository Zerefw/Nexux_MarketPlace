package application.domain.services;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio encargado de liberar las reservas de inventario cuando una orden
 * en estado pendiente de pago es cancelada.
 */
@Service
public class InventoryReleaseDomainService {

    /**
     * Libera las cantidades previamente reservadas para los items de una orden cancelada.
     *
     * @param order          Agregado de la orden que se va a cancelar.
     * @param inventoryItems Registros de inventario donde se encuentran las reservas.
     */
    public void releaseReservedItemsForOrder(Order order, List<InventoryItem> inventoryItems) {
        if (order == null || inventoryItems == null) {
            throw new DomainException("La orden y la lista de inventario son obligatorias para liberar reservas");
        }

        for (OrderItem orderItem : order.getItems()) {
            int remainingToRelease = orderItem.getQuantity();

            List<InventoryItem> matchingStock = inventoryItems.stream()
                    .filter(item -> item.getSku().getCode().equals(orderItem.getSku().getCode()))
                    .toList();

            for (InventoryItem stockItem : matchingStock) {
                if (remainingToRelease <= 0) {
                    break;
                }
                int currentlyReserved = stockItem.getReservedQuantity();
                if (currentlyReserved > 0) {
                    int releaseAmount = Math.min(remainingToRelease, currentlyReserved);
                    stockItem.releaseReservation(releaseAmount);
                    remainingToRelease -= releaseAmount;
                }
            }
        }
    }
}

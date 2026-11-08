package application.domain.services;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio transversal para operaciones consolidadas de inventario sobre ordenes.
 */
@Service
public class InventoryDomainService {

    /**
     * Reserva el stock necesario en las distintas bodegas para cubrir los items de una orden.
     *
     * @param order          Orden de compra cuyos items requieren reserva.
     * @param inventoryItems Lista de registros de inventario disponibles.
     */
    public void reserveStockForOrder(Order order, List<InventoryItem> inventoryItems) {
        if (order == null || inventoryItems == null) {
            throw new DomainException("La orden y los items de inventario son obligatorios");
        }

        for (OrderItem orderItem : order.getItems()) {
            int quantityToReserve = orderItem.getQuantity();

            List<InventoryItem> matchingItems = inventoryItems.stream()
                    .filter(i -> i.getSku().getCode().equals(orderItem.getSku().getCode()))
                    .toList();

            int totalAvailable = matchingItems.stream()
                    .mapToInt(InventoryItem::getAvailableQuantity)
                    .sum();

            if (totalAvailable < quantityToReserve) {
                throw new DomainException("Stock total insuficiente para el SKU: " + orderItem.getSku().getCode());
            }

            int remaining = quantityToReserve;
            for (InventoryItem invItem : matchingItems) {
                if (remaining <= 0) {
                    break;
                }
                int available = invItem.getAvailableQuantity();
                if (available > 0) {
                    int toReserve = Math.min(remaining, available);
                    invItem.reserve(toReserve);
                    remaining -= toReserve;
                }
            }
        }
    }
}

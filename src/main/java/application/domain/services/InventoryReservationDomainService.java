package application.domain.services;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio encargado de la reserva de inventario durante el proceso de checkout.
 * Coordina la asignacion de unidades disponibles en una o multiples bodegas para una orden.
 */
@Service
public class InventoryReservationDomainService {

    /**
     * Ejecuta la reserva de unidades de inventario para todos los items de la orden.
     *
     * @param order          Agregado de la orden en proceso de checkout.
     * @param inventoryItems Registros de inventario asociados a los SKUs de la orden.
     */
    public void reserveItemsForOrder(Order order, List<InventoryItem> inventoryItems) {
        if (order == null || inventoryItems == null) {
            throw new DomainException("La orden y la lista de inventario son requeridas para reservar stock");
        }

        for (OrderItem orderItem : order.getItems()) {
            int requiredQuantity = orderItem.getQuantity();

            List<InventoryItem> matchingStock = inventoryItems.stream()
                    .filter(item -> item.getSku().getCode().equals(orderItem.getSku().getCode()))
                    .toList();

            int totalAvailable = matchingStock.stream()
                    .mapToInt(InventoryItem::getAvailableQuantity)
                    .sum();

            if (totalAvailable < requiredQuantity) {
                throw new DomainException("Stock disponible insuficiente para reservar el SKU: " + orderItem.getSku().getCode());
            }

            int remainingToReserve = requiredQuantity;
            for (InventoryItem stockItem : matchingStock) {
                if (remainingToReserve <= 0) {
                    break;
                }
                int availableInWarehouse = stockItem.getAvailableQuantity();
                if (availableInWarehouse > 0) {
                    int allocation = Math.min(remainingToReserve, availableInWarehouse);
                    stockItem.reserve(allocation);
                    remainingToReserve -= allocation;
                }
            }
        }
    }
}

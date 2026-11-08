package application.domain.services;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio encargado de consolidar la deduccion definitiva de stock fisico
 * y reservado cuando se confirma el pago de una orden.
 */
@Service
public class OrderFulfillmentDomainService {

    /**
     * Deduce del inventario fisico las unidades previamente reservadas para la orden pagada.
     *
     * @param order          Orden de compra cuyo pago ha sido confirmado.
     * @param inventoryItems Registros de inventario con stock reservado.
     */
    public void deductReservedStockForOrder(Order order, List<InventoryItem> inventoryItems) {
        if (order == null || inventoryItems == null) {
            throw new DomainException("La orden y los registros de inventario son obligatorios para el cumplimiento");
        }

        for (OrderItem orderItem : order.getItems()) {
            int remainingToDeduct = orderItem.getQuantity();

            List<InventoryItem> matchingStock = inventoryItems.stream()
                    .filter(item -> item.getSku().getCode().equals(orderItem.getSku().getCode()))
                    .toList();

            int totalReserved = matchingStock.stream()
                    .mapToInt(InventoryItem::getReservedQuantity)
                    .sum();

            if (totalReserved < remainingToDeduct) {
                throw new DomainException("Stock reservado insuficiente para deducir el SKU: " + orderItem.getSku().getCode());
            }

            for (InventoryItem stockItem : matchingStock) {
                if (remainingToDeduct <= 0) {
                    break;
                }
                int reservedInWarehouse = stockItem.getReservedQuantity();
                if (reservedInWarehouse > 0) {
                    int deduction = Math.min(remainingToDeduct, reservedInWarehouse);
                    stockItem.deductStock(deduction);
                    remainingToDeduct -= deduction;
                }
            }
        }
    }
}

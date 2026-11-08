package application.domain.services;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de dominio encargado de reintegrar las unidades fisicas al inventario
 * cuando se procesa una devolucion o reembolso de una orden.
 */
@Service
public class ReturnInventoryDomainService {

    /**
     * Reintegra al stock fisico las cantidades correspondientes a los items de una orden reembolsada.
     *
     * @param order          Orden sobre la cual se aplica el reembolso.
     * @param inventoryItems Registros de inventario donde se reintegrara la mercancia.
     */
    public void returnItemsToStock(Order order, List<InventoryItem> inventoryItems) {
        if (order == null || inventoryItems == null) {
            throw new DomainException("La orden y los registros de inventario son obligatorios para reintegrar stock");
        }

        for (OrderItem orderItem : order.getItems()) {
            List<InventoryItem> matchingStock = inventoryItems.stream()
                    .filter(item -> item.getSku().getCode().equals(orderItem.getSku().getCode()))
                    .toList();

            if (matchingStock.isEmpty()) {
                throw new DomainException("No existe registro de inventario base para reintegrar el SKU: " + orderItem.getSku().getCode());
            }

            InventoryItem targetStock = matchingStock.get(0);
            targetStock.addStock(orderItem.getQuantity());
        }
    }
}

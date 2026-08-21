package application.domain.service;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;

import java.util.List;

public class InventoryDomainService {

    public void reserveStockForOrder(Order order, List<InventoryItem> inventoryItems) {
        if (order == null || inventoryItems == null) {
            throw new DomainException("Order and inventory items are required");
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
                throw new DomainException("Insufficient total stock for SKU: " + orderItem.getSku().getCode());
            }

            int remaining = quantityToReserve;
            for (InventoryItem invItem : matchingItems) {
                if (remaining <= 0) break;
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

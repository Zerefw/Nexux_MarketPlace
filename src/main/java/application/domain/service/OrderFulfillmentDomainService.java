package application.domain.service;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;

import java.util.List;

public class OrderFulfillmentDomainService {

    public void processPaymentAndFulfill(Order order, List<InventoryItem> reservedItems) {
        if (order == null) throw new DomainException("Order is required");

        order.markAsPaid();

        if (reservedItems != null && !reservedItems.isEmpty()) {
            for (OrderItem orderItem : order.getItems()) {
                int quantityToConsume = orderItem.getQuantity();

                List<InventoryItem> matchingItems = reservedItems.stream()
                    .filter(i -> i.getSku().getCode().equals(orderItem.getSku().getCode()))
                    .toList();

                int remaining = quantityToConsume;
                for (InventoryItem invItem : matchingItems) {
                    if (remaining <= 0) break;
                    int reserved = invItem.getReservedQuantity();
                    if (reserved > 0) {
                        int toConsume = Math.min(remaining, reserved);
                        invItem.consumeReserved(toConsume);
                        remaining -= toConsume;
                    }
                }
            }
        }
    }
}

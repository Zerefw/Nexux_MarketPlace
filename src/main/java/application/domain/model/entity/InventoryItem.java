package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Sku;
import application.domain.model.valueobject.StockLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryItem {
    private String id;
    private String warehouseId;
    private Sku sku;
    private int physicalQuantity;
    private int reservedQuantity;
    private int damagedQuantity;
    private StockLocation location;

    public static InventoryItem create(String warehouseId, Sku sku, int initialQuantity, StockLocation location) {
        if (warehouseId == null) throw new DomainException("Warehouse ID is required");
        if (initialQuantity < 0) throw new DomainException("Initial quantity cannot be negative");

        return InventoryItem.builder()
                .warehouseId(warehouseId)
                .sku(sku)
                .physicalQuantity(initialQuantity)
                .reservedQuantity(0)
                .damagedQuantity(0)
                .location(location)
                .build();
    }

    public int getAvailableQuantity() {
        return physicalQuantity - reservedQuantity - damagedQuantity;
    }

    public void reserve(int quantity) {
        if (quantity <= 0) throw new DomainException("Reservation must be positive");
        if (getAvailableQuantity() < quantity) throw new DomainException("Not enough available stock");
        this.reservedQuantity += quantity;
    }

    public void reportDamaged(int quantity) {
        if (quantity <= 0) throw new DomainException("Quantity must be positive");
        if (getAvailableQuantity() < quantity) throw new DomainException("Cannot mark more items damaged than available");
        this.damagedQuantity += quantity;
    }
}

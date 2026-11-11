package application.adapter.out.persistence;

import application.adapter.out.persistence.entity.InventoryJpaEntity;
import application.adapter.out.persistence.repository.InventoryJpaRepository;
import application.domain.model.entity.InventoryItem;
import application.domain.model.valueobject.Sku;
import application.domain.model.valueobject.StockLocation;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida de persistencia para la entidad InventoryItem.
 */
@Component
@RequiredArgsConstructor
public class InventoryPersistenceAdapter implements InventoryRepositoryPort {

    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    public InventoryItem save(InventoryItem item) {
        InventoryJpaEntity jpa = InventoryJpaEntity.builder()
                .id(item.getId())
                .warehouseId(item.getWarehouseId())
                .sku(item.getSku().getCode())
                .physicalQuantity(item.getPhysicalQuantity())
                .reservedQuantity(item.getReservedQuantity())
                .damagedQuantity(item.getDamagedQuantity())
                .aisle(item.getLocation() != null ? item.getLocation().getAisle() : "A1")
                .shelf(item.getLocation() != null ? item.getLocation().getRack() : "R1")
                .level(item.getLocation() != null ? item.getLocation().getShelf() : "S1")
                .build();
        if (jpa.getId() == null) {
            inventoryJpaRepository.findByWarehouseIdAndSku(item.getWarehouseId(), item.getSku().getCode())
                    .ifPresent(existing -> jpa.setId(existing.getId()));
        }
        InventoryJpaEntity saved = inventoryJpaRepository.save(jpa);
        return toDomain(saved);
    }

    @Override
    public Optional<InventoryItem> findById(String id) {
        return inventoryJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<InventoryItem> findByWarehouseIdAndSku(String warehouseId, Sku sku) {
        return inventoryJpaRepository.findByWarehouseIdAndSku(warehouseId, sku.getCode())
                .map(this::toDomain);
    }

    @Override
    public List<InventoryItem> findBySku(Sku sku) {
        return inventoryJpaRepository.findBySku(sku.getCode()).stream()
                .map(this::toDomain)
                .toList();
    }

    private InventoryItem toDomain(InventoryJpaEntity jpa) {
        return InventoryItem.builder()
                .id(jpa.getId())
                .warehouseId(jpa.getWarehouseId())
                .sku(new Sku(jpa.getSku()))
                .physicalQuantity(jpa.getPhysicalQuantity())
                .reservedQuantity(jpa.getReservedQuantity())
                .damagedQuantity(jpa.getDamagedQuantity())
                .location(new StockLocation(jpa.getAisle(), jpa.getShelf(), jpa.getLevel()))
                .build();
    }
}

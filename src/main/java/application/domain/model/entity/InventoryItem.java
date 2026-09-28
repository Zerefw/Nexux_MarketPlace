package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Sku;
import application.domain.model.valueobject.StockLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Entidad que representa un Item de Inventario.
 */
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

    /**
     * Crea un item de inventario.
     */
    public static InventoryItem create(String warehouseId, Sku sku, int initialQuantity, StockLocation location) {
        if (warehouseId == null) throw new DomainException("El ID de la bodega es requerido");
        if (initialQuantity < 0) throw new DomainException("La cantidad inicial no puede ser negativa");

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

    /**
     * Reserva cantidad en inventario.
     */
    public void reserve(int quantity) {
        if (quantity <= 0) throw new DomainException("La reserva debe ser positiva");
        if (getAvailableQuantity() < quantity) throw new DomainException("No hay suficiente stock disponible");
        this.reservedQuantity += quantity;
    }

    /**
     * Reporta items danados.
     */
    public void reportDamaged(int quantity) {
        if (quantity <= 0) throw new DomainException("La cantidad debe ser positiva");
        if (getAvailableQuantity() < quantity) throw new DomainException("No se puede marcar mas items como danados que los disponibles");
        this.damagedQuantity += quantity;
    }
    
    /**
     * Agrega stock fisico.
     */
    public void addStock(int quantity) {
        if (quantity <= 0) throw new DomainException("La cantidad a agregar debe ser positiva");
        this.physicalQuantity += quantity;
    }
    
    /**
     * Deduce stock fisico tras confirmar la reserva.
     */
    public void deductStock(int quantity) {
        if (quantity <= 0) throw new DomainException("La cantidad debe ser positiva");
        if (this.reservedQuantity < quantity) throw new DomainException("La reserva es insuficiente para la deduccion");
        this.reservedQuantity -= quantity;
        this.physicalQuantity -= quantity;
    }
    
    /**
     * Libera stock reservado.
     */
    public void releaseReservation(int quantity) {
        if (quantity <= 0) throw new DomainException("La cantidad debe ser positiva");
        if (this.reservedQuantity < quantity) throw new DomainException("No hay tanta cantidad reservada");
        this.reservedQuantity -= quantity;
    }
}

package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.WarehouseType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Entidad que representa una Bodega.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Warehouse {
    private String id;
    private String ownerId; // marketplace or seller id
    private String name;
    private String locationAddress;
    private WarehouseType type;
    private boolean active;

    /**
     * Crea una nueva bodega.
     */
    public static Warehouse create(String ownerId, String name, String locationAddress, WarehouseType type) {
        if (name == null || name.isBlank()) throw new DomainException("El nombre es requerido");
        
        return Warehouse.builder()
                .ownerId(ownerId)
                .name(name)
                .locationAddress(locationAddress)
                .type(type)
                .active(true)
                .build();
    }
    
    /**
     * Desactiva la bodega.
     */
    public void deactivate() {
        this.active = false;
    }
}

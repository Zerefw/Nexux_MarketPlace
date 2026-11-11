package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.WarehouseType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Entidad que representa una Bodega fisica dentro del Marketplace.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Warehouse {
    private String id;
    private String ownerId;
    private String name;
    private String locationAddress;
    private WarehouseType type;
    private boolean active;

    /**
     * Crea una nueva bodega validando todos sus atributos obligatorios.
     */
    public static Warehouse create(String ownerId, String name, String locationAddress, WarehouseType type) {
        if (ownerId == null || ownerId.isBlank()) {
            throw new DomainException("El ID del propietario de la bodega es requerido");
        }
        if (name == null || name.isBlank()) {
            throw new DomainException("El nombre de la bodega es requerido");
        }
        if (locationAddress == null || locationAddress.isBlank()) {
            throw new DomainException("La direccion de ubicacion de la bodega es requerida");
        }
        if (type == null) {
            throw new DomainException("El tipo de bodega es requerido");
        }

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

package application.domain.ports.in.command.warehouse;

import application.domain.model.valueobject.WarehouseType;

/**
 * Comando para registrar una nueva bodega fisica en el sistema.
 *
 * @param ownerId         Identificador del propietario (Marketplace o Vendedor).
 * @param name            Nombre descriptivo de la bodega.
 * @param locationAddress Direccion fisica de ubicacion.
 * @param type            Tipo de bodega (OWNED o SELLER_MANAGED).
 */
public record RegisterWarehouseCommand(
        String ownerId,
        String name,
        String locationAddress,
        WarehouseType type
) {
}

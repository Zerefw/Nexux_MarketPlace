package application.domain.ports.in.warehouse;

import application.domain.model.entity.Warehouse;
import application.domain.ports.in.command.warehouse.RegisterWarehouseCommand;

/**
 * Puerto de entrada para el caso de uso de registro de bodega.
 */
public interface RegisterWarehouseUseCase {

    /**
     * Registra una nueva bodega fisica en el sistema.
     *
     * @param command Comando con los datos requeridos de la bodega.
     * @return Entidad Warehouse creada y persistida.
     */
    Warehouse execute(RegisterWarehouseCommand command);
}

package application.domain.ports.in.inventory;

import application.domain.ports.in.command.inventory.GetAvailableStockCommand;

/**
 * Puerto de entrada para el caso de uso de consulta consolidada de stock disponible.
 */
public interface GetAvailableStockUseCase {

    /**
     * Calcula y retorna la cantidad total disponible para la venta de un SKU en todas las bodegas.
     *
     * @param command Comando con el SKU a consultar.
     * @return Cantidad total de unidades disponibles.
     */
    int execute(GetAvailableStockCommand command);
}

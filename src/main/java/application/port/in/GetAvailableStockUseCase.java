package application.port.in;

import application.port.in.command.GetAvailableStockCommand;

/**
 * Puerto de entrada para el caso de uso GetAvailableStock.
 */
public interface GetAvailableStockUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(GetAvailableStockCommand command);
}

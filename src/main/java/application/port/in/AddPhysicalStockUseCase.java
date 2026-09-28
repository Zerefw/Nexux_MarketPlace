package application.port.in;

import application.port.in.command.AddPhysicalStockCommand;

/**
 * Puerto de entrada para el caso de uso AddPhysicalStock.
 */
public interface AddPhysicalStockUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(AddPhysicalStockCommand command);
}

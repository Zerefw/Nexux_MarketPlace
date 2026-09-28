package application.domain.ports.in;

import application.domain.ports.in.command.AddPhysicalStockCommand;

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

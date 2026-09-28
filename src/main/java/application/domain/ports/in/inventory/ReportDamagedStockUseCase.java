package application.domain.ports.in;

import application.domain.ports.in.command.ReportDamagedStockCommand;

/**
 * Puerto de entrada para el caso de uso ReportDamagedStock.
 */
public interface ReportDamagedStockUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(ReportDamagedStockCommand command);
}

package application.port.in;

import application.port.in.command.ProcessRefundCommand;

/**
 * Puerto de entrada para el caso de uso ProcessRefund.
 */
public interface ProcessRefundUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(ProcessRefundCommand command);
}

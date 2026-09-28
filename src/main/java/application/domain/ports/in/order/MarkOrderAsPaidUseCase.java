package application.domain.ports.in;

import application.domain.ports.in.command.MarkOrderAsPaidCommand;

/**
 * Puerto de entrada para el caso de uso MarkOrderAsPaid.
 */
public interface MarkOrderAsPaidUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(MarkOrderAsPaidCommand command);
}

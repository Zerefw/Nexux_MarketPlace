package application.domain.ports.in;

import application.domain.ports.in.command.RegisterBuyerCommand;

/**
 * Puerto de entrada para el caso de uso RegisterBuyer.
 */
public interface RegisterBuyerUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(RegisterBuyerCommand command);
}

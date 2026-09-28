package application.port.in;

import application.port.in.command.RegisterSellerCommand;

/**
 * Puerto de entrada para el caso de uso RegisterSeller.
 */
public interface RegisterSellerUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(RegisterSellerCommand command);
}

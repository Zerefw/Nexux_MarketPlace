package application.port.in;

import application.port.in.command.UpdateSellerTaxIdCommand;

/**
 * Puerto de entrada para el caso de uso UpdateSellerTaxId.
 */
public interface UpdateSellerTaxIdUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(UpdateSellerTaxIdCommand command);
}

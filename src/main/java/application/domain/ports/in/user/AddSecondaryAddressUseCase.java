package application.domain.ports.in;

import application.domain.ports.in.command.AddSecondaryAddressCommand;

/**
 * Puerto de entrada para el caso de uso AddSecondaryAddress.
 */
public interface AddSecondaryAddressUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(AddSecondaryAddressCommand command);
}

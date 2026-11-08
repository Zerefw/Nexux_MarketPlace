package application.domain.ports.in.user;

import application.domain.ports.in.command.user.AddSecondaryAddressCommand;

/**
 * Puerto de entrada para agregar una direccion secundaria al perfil de un comprador.
 */
public interface AddSecondaryAddressUseCase {

    /**
     * Agrega una nueva direccion secundaria al comprador.
     *
     * @param command Comando con el identificador del comprador y la direccion.
     */
    void addSecondaryAddress(AddSecondaryAddressCommand command);
}

package application.domain.ports.in.user;

import application.domain.ports.in.command.user.UpdateSellerTaxIdCommand;

/**
 * Puerto de entrada para actualizar el identificador tributario de un vendedor.
 */
public interface UpdateSellerTaxIdUseCase {

    /**
     * Actualiza el identificador fiscal en el perfil del vendedor.
     *
     * @param command Comando con el identificador del vendedor y el nuevo identificador fiscal.
     */
    void updateSellerTaxId(UpdateSellerTaxIdCommand command);
}

package application.domain.ports.in.user;

import application.domain.model.entity.User;
import application.domain.ports.in.command.user.RegisterBuyerCommand;
import application.domain.ports.in.command.user.RegisterSellerCommand;

/**
 * Puerto de entrada unificado para el registro de compradores y vendedores.
 */
public interface RegisterUserUseCase {

    /**
     * Registra un comprador y su perfil comercial asociado.
     *
     * @param command Datos de registro del comprador.
     * @return Entidad User registrada.
     */
    User registerBuyer(RegisterBuyerCommand command);

    /**
     * Registra un vendedor y su perfil de tienda asociado.
     *
     * @param command Datos de registro del vendedor.
     * @return Entidad User registrada.
     */
    User registerSeller(RegisterSellerCommand command);
}

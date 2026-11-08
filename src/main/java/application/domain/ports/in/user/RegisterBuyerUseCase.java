package application.domain.ports.in.user;

import application.domain.model.entity.User;
import application.domain.ports.in.command.user.RegisterBuyerCommand;

/**
 * Puerto de entrada especifico para el caso de uso de registro de comprador.
 */
public interface RegisterBuyerUseCase {

    /**
     * Ejecuta el registro de un nuevo comprador.
     *
     * @param command Comando con los datos requeridos del comprador.
     * @return Entidad User creada.
     */
    User registerBuyer(RegisterBuyerCommand command);
}

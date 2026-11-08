package application.domain.ports.in.user;

import application.domain.model.entity.User;
import application.domain.ports.in.command.user.RegisterSellerCommand;

/**
 * Puerto de entrada especifico para el caso de uso de registro de vendedor.
 */
public interface RegisterSellerUseCase {

    /**
     * Ejecuta el registro de un nuevo vendedor.
     *
     * @param command Comando con los datos requeridos del vendedor.
     * @return Entidad User creada.
     */
    User registerSeller(RegisterSellerCommand command);
}

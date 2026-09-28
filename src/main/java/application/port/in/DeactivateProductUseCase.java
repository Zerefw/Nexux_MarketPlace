package application.port.in;

import application.port.in.command.DeactivateProductCommand;

/**
 * Puerto de entrada para el caso de uso DeactivateProduct.
 */
public interface DeactivateProductUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(DeactivateProductCommand command);
}

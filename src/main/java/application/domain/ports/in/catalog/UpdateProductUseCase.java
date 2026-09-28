package application.domain.ports.in;

import application.domain.ports.in.command.UpdateProductCommand;

/**
 * Puerto de entrada para el caso de uso UpdateProduct.
 */
public interface UpdateProductUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(UpdateProductCommand command);
}

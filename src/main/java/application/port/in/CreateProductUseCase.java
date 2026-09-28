package application.port.in;

import application.port.in.command.CreateProductCommand;

/**
 * Puerto de entrada para el caso de uso CreateProduct.
 */
public interface CreateProductUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(CreateProductCommand command);
}

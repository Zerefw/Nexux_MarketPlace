package application.domain.ports.in;

import application.domain.ports.in.command.ListActiveProductsCommand;

/**
 * Puerto de entrada para el caso de uso ListActiveProducts.
 */
public interface ListActiveProductsUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(ListActiveProductsCommand command);
}

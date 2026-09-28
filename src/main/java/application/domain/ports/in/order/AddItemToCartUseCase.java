package application.domain.ports.in;

import application.domain.ports.in.command.AddItemToCartCommand;

/**
 * Puerto de entrada para el caso de uso AddItemToCart.
 */
public interface AddItemToCartUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(AddItemToCartCommand command);
}

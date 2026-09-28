package application.domain.ports.in;

import application.domain.ports.in.command.FindProductBySkuCommand;

/**
 * Puerto de entrada para el caso de uso FindProductBySku.
 */
public interface FindProductBySkuUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(FindProductBySkuCommand command);
}

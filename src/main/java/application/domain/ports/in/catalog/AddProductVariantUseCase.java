package application.domain.ports.in;

import application.domain.ports.in.command.AddProductVariantCommand;

/**
 * Puerto de entrada para el caso de uso AddProductVariant.
 */
public interface AddProductVariantUseCase {
    /**
     * Ejecuta el caso de uso.
     * @param command Comando con los datos requeridos
     */
    void execute(AddProductVariantCommand command);
}

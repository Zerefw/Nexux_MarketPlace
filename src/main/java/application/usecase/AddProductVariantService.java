package application.usecase;

import application.port.in.AddProductVariantUseCase;
import application.port.in.command.AddProductVariantCommand;
import application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para añadir nuevas variantes a un producto existente.
 */
@Service
@RequiredArgsConstructor
public class AddProductVariantService implements AddProductVariantUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Añade una variante a un producto.
     * @param command Comando con los datos de la variante.
     */
    @Override
    @Transactional
    public void addProductVariant(AddProductVariantCommand command) {
        // Implementacion
    }
}

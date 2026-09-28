package application.usecase;

import application.port.in.DeactivateWarehouseUseCase;
import application.port.in.command.DeactivateWarehouseCommand;
import application.port.out.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para cerrar o cesar operaciones de una bodega.
 */
@Service
@RequiredArgsConstructor
public class DeactivateWarehouseService implements DeactivateWarehouseUseCase {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    /**
     * Desactiva una bodega en el sistema.
     * @param command Comando con la identificacion de la bodega.
     */
    @Override
    @Transactional
    public void deactivateWarehouse(DeactivateWarehouseCommand command) {
        // Implementacion
    }
}

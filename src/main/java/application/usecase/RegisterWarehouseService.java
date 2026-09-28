package application.usecase;

import application.port.in.RegisterWarehouseUseCase;
import application.port.in.command.RegisterWarehouseCommand;
import application.port.out.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para registrar una nueva bodega fisica.
 */
@Service
@RequiredArgsConstructor
public class RegisterWarehouseService implements RegisterWarehouseUseCase {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    /**
     * Registra una bodega en el sistema.
     * @param command Comando con los datos de la bodega.
     */
    @Override
    @Transactional
    public void registerWarehouse(RegisterWarehouseCommand command) {
        // Implementacion
    }
}

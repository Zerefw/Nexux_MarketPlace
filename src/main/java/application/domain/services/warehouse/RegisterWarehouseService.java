package application.domain.services.warehouse;

import application.domain.ports.in.warehouse.RegisterWarehouseUseCase;
import application.domain.ports.in.command.warehouse.RegisterWarehouseCommand;
import application.domain.ports.out.warehouse.WarehouseRepositoryPort;
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
    public void execute(RegisterWarehouseCommand command) {
        application.domain.model.entity.Warehouse warehouse = application.domain.model.entity.Warehouse.create(
            command.ownerId(),
            command.name(),
            command.locationAddress(),
            command.type()
        );
        warehouseRepositoryPort.save(warehouse);
    }
}

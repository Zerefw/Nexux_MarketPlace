package application.domain.services.warehouse;

import application.domain.exception.DomainException;
import application.domain.model.entity.Warehouse;
import application.domain.ports.in.command.warehouse.DeactivateWarehouseCommand;
import application.domain.ports.in.warehouse.DeactivateWarehouseUseCase;
import application.domain.ports.out.warehouse.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para cerrar o cesar operaciones de una bodega.
 * Implementa el caso de uso DeactivateWarehouseUseCase.
 */
@Service
@RequiredArgsConstructor
public class DeactivateWarehouseService implements DeactivateWarehouseUseCase {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    /**
     * Desactiva una bodega en el sistema validando su identificador.
     *
     * @param command Comando con la identificacion de la bodega.
     */
    @Override
    @Transactional
    public void execute(DeactivateWarehouseCommand command) {
        if (command.warehouseId() == null || command.warehouseId().isBlank()) {
            throw new DomainException("El identificador de la bodega es obligatorio");
        }
        Warehouse warehouse = warehouseRepositoryPort.findById(command.warehouseId())
                .orElseThrow(() -> new DomainException("Bodega no encontrada con ID: " + command.warehouseId()));
        warehouse.deactivate();
        warehouseRepositoryPort.save(warehouse);
    }
}

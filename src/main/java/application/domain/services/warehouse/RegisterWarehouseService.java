package application.domain.services.warehouse;

import application.domain.model.entity.Warehouse;
import application.domain.ports.in.command.warehouse.RegisterWarehouseCommand;
import application.domain.ports.in.warehouse.RegisterWarehouseUseCase;
import application.domain.ports.out.warehouse.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para registrar una nueva bodega fisica.
 * Implementa el caso de uso RegisterWarehouseUseCase.
 */
@Service
@RequiredArgsConstructor
public class RegisterWarehouseService implements RegisterWarehouseUseCase {

    private final WarehouseRepositoryPort warehouseRepositoryPort;

    /**
     * Registra una bodega en el sistema validando sus invariantes de dominio.
     *
     * @param command Comando con los datos de la bodega.
     * @return Entidad Warehouse persistida.
     */
    @Override
    @Transactional
    public Warehouse execute(RegisterWarehouseCommand command) {
        Warehouse warehouse = Warehouse.create(
                command.ownerId(),
                command.name(),
                command.locationAddress(),
                command.type()
        );
        return warehouseRepositoryPort.save(warehouse);
    }
}

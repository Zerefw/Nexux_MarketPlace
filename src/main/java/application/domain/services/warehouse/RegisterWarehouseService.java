package application.domain.services.warehouse;

import application.domain.exception.DomainException;
import application.domain.model.entity.Warehouse;
import application.domain.model.valueobject.WarehouseType;
import application.domain.ports.in.command.warehouse.RegisterWarehouseCommand;
import application.domain.ports.in.warehouse.RegisterWarehouseUseCase;
import application.domain.ports.out.user.SellerProfileRepositoryPort;
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
    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;

    /**
     * Registra una bodega en el sistema validando sus invariantes de dominio y que el propietario exista.
     *
     * @param command Comando con los datos de la bodega.
     * @return Entidad Warehouse persistida.
     */
    @Override
    @Transactional
    public Warehouse execute(RegisterWarehouseCommand command) {
        if (command.ownerId() == null || command.ownerId().isBlank()) {
            throw new DomainException("El identificador del propietario (ownerId) de la bodega es obligatorio");
        }
        if (command.type() == WarehouseType.SELLER) {
            sellerProfileRepositoryPort.findBySellerId(command.ownerId())
                    .orElseThrow(() -> new DomainException("No existe un vendedor registrado con ID: " + command.ownerId()));
        }

        Warehouse warehouse = Warehouse.create(
                command.ownerId(),
                command.name(),
                command.locationAddress(),
                command.type()
        );
        return warehouseRepositoryPort.save(warehouse);
    }
}

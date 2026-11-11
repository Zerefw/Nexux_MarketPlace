package application.domain.services.inventory;

import application.domain.exception.DomainException;
import application.domain.model.entity.InventoryItem;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.command.inventory.ReportDamagedStockCommand;
import application.domain.ports.in.inventory.ReportDamagedStockUseCase;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para reportar mermas o inventario danado dentro de una bodega.
 * Implementa el caso de uso ReportDamagedStockUseCase.
 */
@Service
@RequiredArgsConstructor
public class ReportDamagedStockService implements ReportDamagedStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    /**
     * Registra unidades danadas en el inventario de una bodega especifica para un SKU.
     *
     * @param command Comando con los datos de bodega, SKU y cantidad danada.
     * @return Entidad InventoryItem actualizada y persistida.
     */
    @Override
    @Transactional
    public InventoryItem execute(ReportDamagedStockCommand command) {
        if (command.warehouseId() == null || command.warehouseId().isBlank()) {
            throw new DomainException("El identificador de la bodega es obligatorio");
        }
        Sku sku = new Sku(command.sku());
        InventoryItem item = inventoryRepositoryPort.findByWarehouseIdAndSku(command.warehouseId(), sku)
                .orElseThrow(() -> new DomainException(
                        "No se encontro inventario para el SKU " + command.sku() + " en la bodega " + command.warehouseId()
                ));

        item.reportDamaged(command.damagedQuantity());
        return inventoryRepositoryPort.save(item);
    }
}

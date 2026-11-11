package application.domain.services.inventory;

import application.domain.exception.DomainException;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.Warehouse;
import application.domain.model.valueobject.Sku;
import application.domain.model.valueobject.StockLocation;
import application.domain.ports.in.command.inventory.AddPhysicalStockCommand;
import application.domain.ports.in.inventory.AddPhysicalStockUseCase;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import application.domain.ports.out.warehouse.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio de aplicacion para anadir stock fisico (ingreso de mercancia fisica en bodega).
 * Implementa el caso de uso AddPhysicalStockUseCase.
 */
@Service
@RequiredArgsConstructor
public class AddPhysicalStockService implements AddPhysicalStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Ejecuta el ingreso de stock fisico en una bodega para un SKU determinado,
     * verificando previamente que la bodega exista y este activa, y que el SKU exista en el catalogo.
     *
     * @param command Comando que contiene la bodega, el SKU, la cantidad y la ubicacion fisica.
     * @return Entidad InventoryItem persistida con el nuevo balance de stock.
     */
    @Override
    @Transactional
    public InventoryItem execute(AddPhysicalStockCommand command) {
        if (command.warehouseId() == null || command.warehouseId().isBlank()) {
            throw new DomainException("El identificador de la bodega es obligatorio");
        }
        if (command.quantity() <= 0) {
            throw new DomainException("La cantidad de stock fisico a ingresar debe ser mayor a cero");
        }

        Warehouse warehouse = warehouseRepositoryPort.findById(command.warehouseId())
                .orElseThrow(() -> new DomainException("Bodega no encontrada con ID: " + command.warehouseId()));
        if (!warehouse.isActive()) {
            throw new DomainException("No se puede ingresar stock en una bodega desactivada: " + command.warehouseId());
        }

        Sku sku = new Sku(command.sku());
        if (!productRepositoryPort.existsBySku(sku)) {
            throw new DomainException("No existe un producto registrado en el catalogo con el SKU: " + sku.getCode());
        }

        Optional<InventoryItem> existingItem = inventoryRepositoryPort
                .findByWarehouseIdAndSku(command.warehouseId(), sku);

        if (existingItem.isPresent()) {
            InventoryItem item = existingItem.get();
            item.addStock(command.quantity());
            return inventoryRepositoryPort.save(item);
        }

        StockLocation location = new StockLocation(
                command.aisle(),
                command.shelf(),
                command.level()
        );
        InventoryItem newItem = InventoryItem.create(
                command.warehouseId(),
                sku,
                command.quantity(),
                location
        );
        return inventoryRepositoryPort.save(newItem);
    }
}

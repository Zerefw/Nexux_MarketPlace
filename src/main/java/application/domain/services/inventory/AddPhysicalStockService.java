package application.domain.services.inventory;

import application.domain.exception.DomainException;
import application.domain.model.entity.InventoryItem;
import application.domain.model.valueobject.Sku;
import application.domain.model.valueobject.StockLocation;
import application.domain.ports.in.command.inventory.AddPhysicalStockCommand;
import application.domain.ports.in.inventory.AddPhysicalStockUseCase;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
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

    /**
     * Ejecuta el ingreso de stock fisico en una bodega para un SKU determinado.
     * Si el registro de inventario ya existe en la bodega, incrementa su cantidad fisica;
     * de lo contrario, inicializa un nuevo registro con su ubicacion topografica.
     *
     * @param command Comando que contiene la bodega, el SKU, la cantidad y la ubicacion fisica.
     * @return Entidad InventoryItem persistida con el nuevo balance de stock.
     */
    @Override
    @Transactional
    public InventoryItem execute(AddPhysicalStockCommand command) {
        if (command.quantity() <= 0) {
            throw new DomainException("La cantidad de stock fisico a ingresar debe ser mayor a cero");
        }
        Sku sku = new Sku(command.sku());

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

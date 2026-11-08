package application.domain.services.inventory;

import application.domain.model.entity.InventoryItem;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.command.inventory.GetAvailableStockCommand;
import application.domain.ports.in.inventory.GetAvailableStockUseCase;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicacion para consultar de forma consolidada la disponibilidad de stock.
 * Implementa el caso de uso GetAvailableStockUseCase.
 */
@Service
@RequiredArgsConstructor
public class GetAvailableStockService implements GetAvailableStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    /**
     * Consulta todos los registros de inventario asociados a un SKU en las distintas bodegas
     * y suma sus cantidades disponibles netas (fisico menos reservado menos danado).
     *
     * @param command Comando que contiene el SKU a consultar.
     * @return Cantidad total disponible para la venta.
     */
    @Override
    @Transactional(readOnly = true)
    public int execute(GetAvailableStockCommand command) {
        Sku sku = new Sku(command.sku());
        List<InventoryItem> items = inventoryRepositoryPort.findBySku(sku);
        return items.stream()
                .mapToInt(InventoryItem::getAvailableQuantity)
                .sum();
    }
}

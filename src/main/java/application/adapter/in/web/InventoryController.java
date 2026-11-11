package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.domain.model.entity.InventoryItem;
import application.domain.ports.in.command.inventory.AddPhysicalStockCommand;
import application.domain.ports.in.command.inventory.GetAvailableStockCommand;
import application.domain.ports.in.command.inventory.ReportDamagedStockCommand;
import application.domain.ports.in.inventory.AddPhysicalStockUseCase;
import application.domain.ports.in.inventory.GetAvailableStockUseCase;
import application.domain.ports.in.inventory.ReportDamagedStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para la administracion de inventario fisico, consultas de stock y mermas.
 */
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final AddPhysicalStockUseCase addPhysicalStockUseCase;
    private final GetAvailableStockUseCase getAvailableStockUseCase;
    private final ReportDamagedStockUseCase reportDamagedStockUseCase;

    /**
     * Registra el ingreso de stock fisico en una bodega para un SKU determinado.
     */
    @PostMapping("/stock")
    public ResponseEntity<ApiResponse<InventoryItem>> addPhysicalStock(@RequestBody AddPhysicalStockCommand command) {
        InventoryItem item = addPhysicalStockUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Stock fisico agregado exitosamente", item));
    }

    /**
     * Consulta la cantidad disponible consolidada para un SKU en todas las bodegas.
     */
    @GetMapping("/stock/{sku}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAvailableStock(@PathVariable String sku) {
        int available = getAvailableStockUseCase.execute(new GetAvailableStockCommand(sku));
        return ResponseEntity.ok(ApiResponse.ok("Stock disponible consultado", Map.of("sku", sku, "availableQuantity", available)));
    }

    /**
     * Reporta unidades danadas o mermas en una bodega especifica.
     */
    @PostMapping("/damaged")
    public ResponseEntity<ApiResponse<InventoryItem>> reportDamagedStock(@RequestBody ReportDamagedStockCommand command) {
        InventoryItem item = reportDamagedStockUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Merma registrada exitosamente", item));
    }
}

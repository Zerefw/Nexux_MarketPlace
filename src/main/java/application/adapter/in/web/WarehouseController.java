package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.domain.model.entity.Warehouse;
import application.domain.ports.in.command.warehouse.DeactivateWarehouseCommand;
import application.domain.ports.in.command.warehouse.RegisterWarehouseCommand;
import application.domain.ports.in.warehouse.DeactivateWarehouseUseCase;
import application.domain.ports.in.warehouse.RegisterWarehouseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la administracion de bodegas fisicas.
 */
@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final RegisterWarehouseUseCase registerWarehouseUseCase;
    private final DeactivateWarehouseUseCase deactivateWarehouseUseCase;

    /**
     * Registra una nueva bodega fisica en el sistema.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Warehouse>> registerWarehouse(@RequestBody RegisterWarehouseCommand command) {
        Warehouse warehouse = registerWarehouseUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Bodega registrada exitosamente", warehouse));
    }

    /**
     * Desactiva una bodega fisica existente.
     */
    @PutMapping("/{warehouseId}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivateWarehouse(@PathVariable String warehouseId) {
        deactivateWarehouseUseCase.execute(new DeactivateWarehouseCommand(warehouseId));
        return ResponseEntity.ok(ApiResponse.ok("Bodega desactivada exitosamente", warehouseId));
    }
}

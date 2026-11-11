package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.logistics.DeliverOrderCommand;
import application.domain.ports.in.command.logistics.ShipOrderCommand;
import application.domain.ports.in.logistics.DeliverOrderUseCase;
import application.domain.ports.in.logistics.ShipOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para las operaciones logisticas de despacho y entrega de ordenes.
 */
@RestController
@RequestMapping("/api/v1/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    private final ShipOrderUseCase shipOrderUseCase;
    private final DeliverOrderUseCase deliverOrderUseCase;

    /**
     * Despacha fisicamente una orden pagada registrando guia y transportadora.
     */
    @PostMapping("/ship")
    public ResponseEntity<ApiResponse<Order>> shipOrder(@RequestBody ShipOrderCommand command) {
        Order shippedOrder = shipOrderUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Orden despachada exitosamente", shippedOrder));
    }

    /**
     * Confirma la entrega final de una orden despachada.
     */
    @PostMapping("/deliver")
    public ResponseEntity<ApiResponse<Order>> deliverOrder(@RequestBody DeliverOrderCommand command) {
        Order deliveredOrder = deliverOrderUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Orden entregada y finalizada exitosamente", deliveredOrder));
    }
}

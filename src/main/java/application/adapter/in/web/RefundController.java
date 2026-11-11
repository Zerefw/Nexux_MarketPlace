package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.refund.ProcessRefundCommand;
import application.domain.ports.in.refund.ProcessRefundUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para el procesamiento de devoluciones y reembolsos de ordenes.
 */
@RestController
@RequestMapping("/api/v1/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final ProcessRefundUseCase processRefundUseCase;

    /**
     * Procesa el reembolso de una orden y reintegra las unidades fisicas al inventario.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Order>> processRefund(@RequestBody ProcessRefundCommand command) {
        Order refundedOrder = processRefundUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Reembolso procesado y stock reintegrado exitosamente", refundedOrder));
    }
}

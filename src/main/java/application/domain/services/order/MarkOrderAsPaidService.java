package application.domain.services.order;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import application.domain.ports.in.command.order.MarkOrderAsPaidCommand;
import application.domain.ports.in.order.MarkOrderAsPaidUseCase;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import application.domain.ports.out.order.OrderRepositoryPort;
import application.domain.services.OrderFulfillmentDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de aplicacion para registrar la confirmacion financiera de una orden pagada.
 * Implementa el caso de uso MarkOrderAsPaidUseCase.
 */
@Service
@RequiredArgsConstructor
public class MarkOrderAsPaidService implements MarkOrderAsPaidUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final OrderFulfillmentDomainService orderFulfillmentDomainService;

    /**
     * Valida la referencia de pago, deduce en firme las unidades reservadas del inventario fisico
     * a traves del servicio de dominio y transiciona la orden al estado PAID.
     *
     * @param command Comando con el identificador de la orden y la referencia de pago.
     * @return Agregado Order en estado PAID.
     */
    @Override
    @Transactional
    public Order execute(MarkOrderAsPaidCommand command) {
        if (command.orderId() == null || command.orderId().isBlank()) {
            throw new DomainException("El identificador de la orden es obligatorio");
        }
        if (command.paymentReference() == null || command.paymentReference().isBlank()) {
            throw new DomainException("La referencia de pago es obligatoria para confirmar el pago");
        }

        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            inventoryItems.addAll(inventoryRepositoryPort.findBySku(item.getSku()));
        }

        orderFulfillmentDomainService.deductReservedStockForOrder(order, inventoryItems);

        for (InventoryItem updatedItem : inventoryItems) {
            inventoryRepositoryPort.save(updatedItem);
        }

        order.markAsPaid();
        return orderRepositoryPort.save(order);
    }
}

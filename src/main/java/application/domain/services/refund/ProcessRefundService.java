package application.domain.services.refund;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.InventoryItem;
import application.domain.model.entity.OrderItem;
import application.domain.ports.in.command.refund.ProcessRefundCommand;
import application.domain.ports.in.refund.ProcessRefundUseCase;
import application.domain.ports.out.inventory.InventoryRepositoryPort;
import application.domain.ports.out.order.OrderRepositoryPort;
import application.domain.services.ReturnInventoryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de aplicacion para iniciar el proceso de devolucion de dinero y reintegrar stock.
 * Implementa el caso de uso ProcessRefundUseCase.
 */
@Service
@RequiredArgsConstructor
public class ProcessRefundService implements ProcessRefundUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ReturnInventoryDomainService returnInventoryDomainService;

    /**
     * Procesa el reembolso de una orden elegible, reintegra el stock fisico en las bodegas
     * mediante el servicio de dominio y cambia el estado de la orden a REFUNDED.
     *
     * @param command Comando con el identificador de la orden y la razon de devolucion.
     * @return Agregado Order en estado REFUNDED.
     */
    @Override
    @Transactional
    public Order execute(ProcessRefundCommand command) {
        if (command.reason() == null || command.reason().isBlank()) {
            throw new DomainException("El motivo del reembolso es obligatorio");
        }

        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        List<InventoryItem> inventoryItems = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            inventoryItems.addAll(inventoryRepositoryPort.findBySku(item.getSku()));
        }

        returnInventoryDomainService.returnItemsToStock(order, inventoryItems);

        for (InventoryItem updatedItem : inventoryItems) {
            inventoryRepositoryPort.save(updatedItem);
        }

        order.refund();
        return orderRepositoryPort.save(order);
    }
}

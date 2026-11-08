package application.domain.services.logistics;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.logistics.DeliverOrderCommand;
import application.domain.ports.in.logistics.DeliverOrderUseCase;
import application.domain.ports.out.order.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para cerrar el ciclo de entrega de una orden.
 * Implementa el caso de uso DeliverOrderUseCase.
 */
@Service
@RequiredArgsConstructor
public class DeliverOrderService implements DeliverOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Confirma la recepcion fisica del pedido por parte del cliente y transiciona
     * la orden al estado DELIVERED_FINALIZED.
     *
     * @param command Comando con el identificador de la orden y quien recibe.
     * @return Agregado Order finalizado.
     */
    @Override
    @Transactional
    public Order execute(DeliverOrderCommand command) {
        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        order.deliver();
        return orderRepositoryPort.save(order);
    }
}

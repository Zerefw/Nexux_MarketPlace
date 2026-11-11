package application.domain.services.logistics;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.logistics.ShipOrderCommand;
import application.domain.ports.in.logistics.ShipOrderUseCase;
import application.domain.ports.out.order.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para despachar una orden fisicamente.
 * Implementa el caso de uso ShipOrderUseCase.
 */
@Service
@RequiredArgsConstructor
public class ShipOrderService implements ShipOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Valida la informacion de guia de transporte y transiciona la orden al estado SHIPPED.
     *
     * @param command Comando con el identificador de la orden, numero de guia y transportadora.
     * @return Agregado Order actualizado en estado SHIPPED.
     */
    @Override
    @Transactional
    public Order execute(ShipOrderCommand command) {
        if (command.orderId() == null || command.orderId().isBlank()) {
            throw new DomainException("El identificador de la orden es obligatorio");
        }
        if (command.trackingNumber() == null || command.trackingNumber().isBlank()) {
            throw new DomainException("El numero de guia es obligatorio para despachar la orden");
        }
        if (command.carrierName() == null || command.carrierName().isBlank()) {
            throw new DomainException("El nombre de la transportadora es obligatorio");
        }

        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        order.ship();
        return orderRepositoryPort.save(order);
    }
}

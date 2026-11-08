package application.domain.services.order;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.command.order.RemoveItemFromCartCommand;
import application.domain.ports.in.order.RemoveItemFromCartUseCase;
import application.domain.ports.out.order.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para quitar items de un carrito de compras.
 * Implementa el caso de uso RemoveItemFromCartUseCase.
 */
@Service
@RequiredArgsConstructor
public class RemoveItemFromCartService implements RemoveItemFromCartUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Elimina un producto especifico de la orden en estado CART y recalcula su monto total.
     *
     * @param command Comando con el identificador de la orden y el SKU a remover.
     * @return Agregado Order actualizado.
     */
    @Override
    @Transactional
    public Order execute(RemoveItemFromCartCommand command) {
        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        Sku sku = new Sku(command.sku());
        order.removeItem(sku);
        return orderRepositoryPort.save(order);
    }
}

package application.domain.services.order;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.OrderItem;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.command.order.AddItemToCartCommand;
import application.domain.ports.in.order.AddItemToCartUseCase;
import application.domain.ports.out.order.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para anadir items a un carrito existente y recalcular sus totales.
 * Implementa el caso de uso AddItemToCartUseCase.
 */
@Service
@RequiredArgsConstructor
public class AddItemToCartService implements AddItemToCartUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Recupera el carrito de compras, construye el nuevo item validando precio y cantidad,
     * lo incorpora al agregado Order y persiste el estado actualizado.
     *
     * @param command Comando con los datos de la orden, SKU, cantidad y precio unitario.
     * @return Agregado Order actualizado.
     */
    @Override
    @Transactional
    public Order execute(AddItemToCartCommand command) {
        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        Sku sku = new Sku(command.sku());
        Money unitPrice = new Money(command.unitPriceAmount(), command.currency());
        OrderItem newItem = OrderItem.create(sku, command.quantity(), unitPrice);

        order.addItem(newItem);
        return orderRepositoryPort.save(order);
    }
}

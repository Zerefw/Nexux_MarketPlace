package application.domain.services.order;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.CreateCartCommand;
import application.domain.ports.in.order.CreateCartUseCase;
import application.domain.ports.out.order.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para inicializar una orden en estado CART (crear carrito de compras).
 * Implementa el caso de uso CreateCartUseCase.
 */
@Service
@RequiredArgsConstructor
public class CreateCartService implements CreateCartUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Crea un nuevo carrito de compras vacio para el comprador especificado.
     *
     * @param command Comando que contiene el identificador del comprador y la moneda.
     * @return Agregado Order inicializado y persistido.
     */
    @Override
    @Transactional
    public Order execute(CreateCartCommand command) {
        Order cart = Order.createCart(command.buyerId(), command.currency());
        return orderRepositoryPort.save(cart);
    }
}

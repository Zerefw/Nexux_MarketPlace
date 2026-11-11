package application.domain.services.order;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.CreateCartCommand;
import application.domain.ports.in.order.CreateCartUseCase;
import application.domain.ports.out.order.OrderRepositoryPort;
import application.domain.ports.out.user.BuyerProfileRepositoryPort;
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
    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;

    /**
     * Crea un nuevo carrito de compras vacio verificando previamente que el comprador exista.
     *
     * @param command Comando que contiene el identificador del comprador y la moneda.
     * @return Agregado Order inicializado y persistido.
     */
    @Override
    @Transactional
    public Order execute(CreateCartCommand command) {
        if (command.buyerId() == null || command.buyerId().isBlank()) {
            throw new DomainException("El identificador del comprador (buyerId) es obligatorio");
        }
        buyerProfileRepositoryPort.findByBuyerId(command.buyerId())
                .orElseThrow(() -> new DomainException("No existe un comprador registrado con ID: " + command.buyerId()));

        Order cart = Order.createCart(command.buyerId(), command.currency());
        return orderRepositoryPort.save(cart);
    }
}

package application.domain.ports.in.order;

import application.domain.model.aggregate.Order;
import application.domain.ports.in.command.order.CreateCartCommand;

/**
 * Puerto de entrada para el caso de uso de creacion de carrito de compras.
 */
public interface CreateCartUseCase {

    /**
     * Crea una nueva orden en estado CART para un comprador.
     *
     * @param command Comando con el comprador y la moneda.
     * @return Agregado Order inicializado en estado CART.
     */
    Order execute(CreateCartCommand command);
}

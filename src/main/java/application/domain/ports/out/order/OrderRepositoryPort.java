package application.domain.ports.out.order;

import application.domain.model.aggregate.Order;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia y consulta del agregado Order.
 */
public interface OrderRepositoryPort {

    /**
     * Guarda o actualiza una orden de compra.
     *
     * @param order Agregado de dominio Order.
     * @return Orden persistida.
     */
    Order save(Order order);

    /**
     * Busca una orden por su identificador unico.
     *
     * @param id Identificador de la orden.
     * @return Optional con la orden encontrada o vacio.
     */
    Optional<Order> findById(String id);

    /**
     * Recupera todas las ordenes asociadas a un comprador.
     *
     * @param buyerId Identificador del comprador.
     * @return Lista de ordenes del comprador.
     */
    List<Order> findByBuyerId(String buyerId);
}

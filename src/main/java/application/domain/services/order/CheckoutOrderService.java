package application.domain.servicess;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.CheckoutOrderUseCase;
import application.domain.ports.in.command.CheckoutOrderCommand;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.services.InventoryReservationDomainService;

/**
 * Servicio de aplicacion para transicionar una orden de CART a PENDING_PAYMENT, reservando inventario.
 * Implementa el caso de uso CheckoutOrderUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutOrderService implements CheckoutOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryReservationDomainService inventoryReservationDomainService;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(CheckoutOrderCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

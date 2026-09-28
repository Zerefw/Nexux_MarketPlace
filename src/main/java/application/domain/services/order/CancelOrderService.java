package application.domain.servicess;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.CancelOrderUseCase;
import application.domain.ports.in.command.CancelOrderCommand;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.services.InventoryReleaseDomainService;

/**
 * Servicio de aplicacion para cancelar una orden no pagada, liberando inventario si es necesario.
 * Implementa el caso de uso CancelOrderUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final InventoryReleaseDomainService inventoryReleaseDomainService;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(CancelOrderCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

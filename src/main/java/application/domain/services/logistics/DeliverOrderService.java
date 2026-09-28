package application.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.DeliverOrderUseCase;
import application.domain.ports.in.command.DeliverOrderCommand;
import application.domain.ports.out.OrderRepositoryPort;

/**
 * Servicio de aplicacion para cerrar el ciclo de entrega de una orden.
 * Implementa el caso de uso DeliverOrderUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliverOrderService implements DeliverOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(DeliverOrderCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

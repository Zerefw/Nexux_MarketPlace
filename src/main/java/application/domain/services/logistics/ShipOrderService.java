package application.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.ShipOrderUseCase;
import application.domain.ports.in.command.ShipOrderCommand;
import application.domain.ports.out.OrderRepositoryPort;

/**
 * Servicio de aplicacion para despachar una orden fisicamente.
 * Implementa el caso de uso ShipOrderUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ShipOrderService implements ShipOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(ShipOrderCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

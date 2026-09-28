package application.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.RemoveItemFromCartUseCase;
import application.domain.ports.in.command.RemoveItemFromCartCommand;
import application.domain.ports.out.OrderRepositoryPort;

/**
 * Servicio de aplicacion para quitar items de un carrito de compras.
 * Implementa el caso de uso RemoveItemFromCartUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RemoveItemFromCartService implements RemoveItemFromCartUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(RemoveItemFromCartCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

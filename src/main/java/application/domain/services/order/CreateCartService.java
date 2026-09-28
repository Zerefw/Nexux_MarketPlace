package application.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.CreateCartUseCase;
import application.domain.ports.in.command.CreateCartCommand;
import application.domain.ports.out.OrderRepositoryPort;

/**
 * Servicio de aplicacion para inicializar una orden en estado CART (crear carrito).
 * Implementa el caso de uso CreateCartUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CreateCartService implements CreateCartUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(CreateCartCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

package application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.port.in.AddItemToCartUseCase;
import application.port.in.command.AddItemToCartCommand;
import application.port.out.OrderRepositoryPort;

/**
 * Servicio de aplicacion para añadir items a un carrito existente y recalcular totales.
 * Implementa el caso de uso AddItemToCartUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AddItemToCartService implements AddItemToCartUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(AddItemToCartCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

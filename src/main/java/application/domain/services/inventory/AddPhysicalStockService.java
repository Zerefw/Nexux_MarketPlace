package application.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.AddPhysicalStockUseCase;
import application.domain.ports.in.command.AddPhysicalStockCommand;
import application.domain.ports.out.InventoryRepositoryPort;

/**
 * Servicio de aplicacion para aÃ±adir stock fisico (ingreso de mercancia fisica).
 * Implementa el caso de uso AddPhysicalStockUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AddPhysicalStockService implements AddPhysicalStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(AddPhysicalStockCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

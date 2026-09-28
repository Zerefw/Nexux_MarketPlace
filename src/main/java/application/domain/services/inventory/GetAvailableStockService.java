package application.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.GetAvailableStockUseCase;
import application.domain.ports.in.command.GetAvailableStockCommand;
import application.domain.ports.out.InventoryRepositoryPort;

/**
 * Servicio de aplicacion para consultar de forma consolidada la disponibilidad de stock.
 * Implementa el caso de uso GetAvailableStockUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GetAvailableStockService implements GetAvailableStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(GetAvailableStockCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

package application.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.ReportDamagedStockUseCase;
import application.domain.ports.in.command.ReportDamagedStockCommand;
import application.domain.ports.out.InventoryRepositoryPort;

/**
 * Servicio de aplicacion para reportar mermas o inventario daÃ±ado.
 * Implementa el caso de uso ReportDamagedStockUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReportDamagedStockService implements ReportDamagedStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(ReportDamagedStockCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

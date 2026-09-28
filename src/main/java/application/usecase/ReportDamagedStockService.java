package application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.port.in.ReportDamagedStockUseCase;
import application.port.in.command.ReportDamagedStockCommand;
import application.port.out.InventoryRepositoryPort;

/**
 * Servicio de aplicacion para reportar mermas o inventario dañado.
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

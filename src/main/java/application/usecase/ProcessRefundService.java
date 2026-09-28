package application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.port.in.ProcessRefundUseCase;
import application.port.in.command.ProcessRefundCommand;
import application.port.out.OrderRepositoryPort;
import application.domain.service.ReturnInventoryDomainService;

/**
 * Servicio de aplicacion para iniciar el proceso de devolucion de dinero y reintegrar stock.
 * Implementa el caso de uso ProcessRefundUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProcessRefundService implements ProcessRefundUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ReturnInventoryDomainService returnInventoryDomainService;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(ProcessRefundCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

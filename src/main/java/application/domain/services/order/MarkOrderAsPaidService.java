package application.domain.servicess;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import application.domain.ports.in.MarkOrderAsPaidUseCase;
import application.domain.ports.in.command.MarkOrderAsPaidCommand;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.services.OrderFulfillmentDomainService;

/**
 * Servicio de aplicacion para registrar la confirmacion financiera de una orden pagada.
 * Implementa el caso de uso MarkOrderAsPaidUseCase.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MarkOrderAsPaidService implements MarkOrderAsPaidUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFulfillmentDomainService orderFulfillmentDomainService;

    /**
     * Ejecuta la logica de negocio principal para el caso de uso.
     * 
     * @param command Comando que contiene los parametros de entrada necesarios.
     */
    @Override
    public void execute(MarkOrderAsPaidCommand command) {
        // TODO: Implementar la logica de dominio delegando en entidades y servicios inyectados.
    }
}

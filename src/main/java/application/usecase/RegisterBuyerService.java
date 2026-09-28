package application.usecase;

import application.port.in.RegisterBuyerUseCase;
import application.port.in.command.RegisterBuyerCommand;
import application.port.out.UserRepositoryPort;
import application.port.out.BuyerProfileRepositoryPort;
import application.port.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion encargado de registrar un nuevo comprador en el sistema.
 * Implementa el puerto de entrada RegisterBuyerUseCase.
 */
@Service
@RequiredArgsConstructor
public class RegisterBuyerService implements RegisterBuyerUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    /**
     * Registra un comprador de manera transaccional.
     * @param command Comando que contiene los datos del comprador a registrar.
     */
    @Override
    @Transactional
    public void registerBuyer(RegisterBuyerCommand command) {
        // Implementacion
    }
}

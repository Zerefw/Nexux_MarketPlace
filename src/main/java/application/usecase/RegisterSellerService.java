package application.usecase;

import application.port.in.RegisterSellerUseCase;
import application.port.in.command.RegisterSellerCommand;
import application.port.out.UserRepositoryPort;
import application.port.out.SellerProfileRepositoryPort;
import application.port.out.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion encargado de registrar un nuevo vendedor en el sistema.
 * Implementa el puerto de entrada RegisterSellerUseCase.
 */
@Service
@RequiredArgsConstructor
public class RegisterSellerService implements RegisterSellerUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    /**
     * Registra un vendedor de manera transaccional.
     * @param command Comando que contiene los datos del vendedor a registrar.
     */
    @Override
    @Transactional
    public void registerSeller(RegisterSellerCommand command) {
        // Implementacion
    }
}

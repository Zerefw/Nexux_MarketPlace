package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.adapter.in.web.dto.UserResponseDTO;
import application.config.JwtTokenProvider;
import application.domain.exception.DomainException;
import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;
import application.domain.ports.in.command.user.RegisterBuyerCommand;
import application.domain.ports.in.command.user.RegisterSellerCommand;
import application.domain.ports.in.user.RegisterBuyerUseCase;
import application.domain.ports.in.user.RegisterSellerUseCase;
import application.domain.ports.out.user.PasswordEncoderPort;
import application.domain.ports.out.user.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST (Adaptador de entrada) para el registro y autenticacion JWT de usuarios.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterBuyerUseCase registerBuyerUseCase;
    private final RegisterSellerUseCase registerSellerUseCase;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registra un nuevo usuario con rol de comprador y retorna sus datos junto con su token JWT.
     */
    @PostMapping("/register/buyer")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerBuyer(@RequestBody RegisterBuyerCommand command) {
        User registeredUser = registerBuyerUseCase.registerBuyer(command);
        UserResponseDTO responseDto = UserResponseDTO.fromDomain(registeredUser);
        return ResponseEntity.ok(ApiResponse.ok("Comprador registrado exitosamente", responseDto));
    }

    /**
     * Registra un nuevo usuario con rol de vendedor y retorna sus datos.
     */
    @PostMapping("/register/seller")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerSeller(@RequestBody RegisterSellerCommand command) {
        User registeredUser = registerSellerUseCase.registerSeller(command);
        UserResponseDTO responseDto = UserResponseDTO.fromDomain(registeredUser);
        return ResponseEntity.ok(ApiResponse.ok("Vendedor registrado exitosamente", responseDto));
    }

    /**
     * Autentica a un usuario mediante correo y contrasena y emite un token JWT firmado.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody Map<String, String> credentials) {
        String rawEmail = credentials.get("email");
        String rawPassword = credentials.get("password");

        if (rawEmail == null || rawEmail.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            throw new DomainException("El correo electronico y la contrasena son requeridos para iniciar sesion");
        }

        Email email = new Email(rawEmail);
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new DomainException("Credenciales invalidas"));

        if (!user.isActive()) {
            throw new DomainException("La cuenta del usuario se encuentra suspendida");
        }

        String encodedPassword = userRepositoryPort.findEncodedPasswordByEmail(email)
                .orElseThrow(() -> new DomainException("Credenciales invalidas"));

        if (!passwordEncoderPort.matches(rawPassword, encodedPassword)) {
            throw new DomainException("Credenciales invalidas");
        }

        String token = jwtTokenProvider.generateToken(user);
        Map<String, Object> payload = Map.of(
                "token", token,
                "user", UserResponseDTO.fromDomain(user)
        );
        return ResponseEntity.ok(ApiResponse.ok("Autenticacion exitosa", payload));
    }
}

package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.adapter.in.web.dto.UserResponseDTO;
import application.domain.model.entity.User;
import application.domain.ports.in.command.user.RegisterBuyerCommand;
import application.domain.ports.in.command.user.RegisterSellerCommand;
import application.domain.ports.in.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST (Adaptador de entrada) para el registro y autenticacion de usuarios.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register/buyer")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerBuyer(@RequestBody RegisterBuyerCommand command) {
        User registeredUser = registerUserUseCase.registerBuyer(command);
        UserResponseDTO responseDto = UserResponseDTO.fromDomain(registeredUser);
        return ResponseEntity.ok(ApiResponse.ok("Buyer successfully registered", responseDto));
    }

    @PostMapping("/register/seller")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerSeller(@RequestBody RegisterSellerCommand command) {
        User registeredUser = registerUserUseCase.registerSeller(command);
        UserResponseDTO responseDto = UserResponseDTO.fromDomain(registeredUser);
        return ResponseEntity.ok(ApiResponse.ok("Seller successfully registered", responseDto));
    }
}

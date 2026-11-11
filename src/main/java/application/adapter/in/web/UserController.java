package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.domain.ports.in.command.user.ActivateUserCommand;
import application.domain.ports.in.command.user.AddSecondaryAddressCommand;
import application.domain.ports.in.command.user.SuspendUserCommand;
import application.domain.ports.in.command.user.UpdateSellerTaxIdCommand;
import application.domain.ports.in.user.ActivateUserUseCase;
import application.domain.ports.in.user.AddSecondaryAddressUseCase;
import application.domain.ports.in.user.SuspendUserUseCase;
import application.domain.ports.in.user.UpdateSellerTaxIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestion de cuentas y perfiles de usuarios (compradores y vendedores).
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final ActivateUserUseCase activateUserUseCase;
    private final SuspendUserUseCase suspendUserUseCase;
    private final AddSecondaryAddressUseCase addSecondaryAddressUseCase;
    private final UpdateSellerTaxIdUseCase updateSellerTaxIdUseCase;

    /**
     * Reactiva la cuenta de un usuario previamente suspendido.
     */
    @PutMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<String>> activateUser(@PathVariable String userId) {
        activateUserUseCase.activateUser(new ActivateUserCommand(userId));
        return ResponseEntity.ok(ApiResponse.ok("Usuario reactivado exitosamente", userId));
    }

    /**
     * Suspende preventivamente la cuenta de un usuario.
     */
    @PutMapping("/{userId}/suspend")
    public ResponseEntity<ApiResponse<String>> suspendUser(@PathVariable String userId) {
        suspendUserUseCase.suspendUser(new SuspendUserCommand(userId));
        return ResponseEntity.ok(ApiResponse.ok("Usuario suspendido exitosamente", userId));
    }

    /**
     * Agrega una direccion secundaria al perfil de un comprador.
     */
    @PostMapping("/buyers/secondary-address")
    public ResponseEntity<ApiResponse<String>> addSecondaryAddress(@RequestBody AddSecondaryAddressCommand command) {
        addSecondaryAddressUseCase.addSecondaryAddress(command);
        return ResponseEntity.ok(ApiResponse.ok("Direccion secundaria agregada exitosamente", command.address()));
    }

    /**
     * Actualiza el identificador tributario en el perfil de un vendedor.
     */
    @PutMapping("/sellers/tax-id")
    public ResponseEntity<ApiResponse<String>> updateSellerTaxId(@RequestBody UpdateSellerTaxIdCommand command) {
        updateSellerTaxIdUseCase.updateSellerTaxId(command);
        return ResponseEntity.ok(ApiResponse.ok("Identificador fiscal actualizado exitosamente", command.taxId()));
    }
}

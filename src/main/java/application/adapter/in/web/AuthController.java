// Paquete que define los controladores REST (Adaptador de entrada)
package application.adapter.in.web;

// Importaciones de los DTOs de respuesta estandarizada
import application.adapter.in.web.dto.ApiResponse;
// ImportaciÃ³n del DTO de usuario para ocultar la entidad de dominio original
import application.adapter.in.web.dto.UserResponseDTO;
// ImportaciÃ³n de la Entidad pura de Dominio
import application.domain.model.entity.User;
// ImportaciÃ³n del Puerto de Entrada (Caso de uso)
import application.domain.ports.in.RegisterUserUseCase;
// Importaciones de los comandos (DTOs de entrada) para el caso de uso
import application.domain.ports.in.command.RegisterBuyerCommand;
import application.domain.ports.in.command.RegisterSellerCommand;
// ImportaciÃ³n de anotaciÃ³n Lombok para inyecciÃ³n de dependencias a travÃ©s de constructor
import lombok.RequiredArgsConstructor;
// ImportaciÃ³n de utilidades de Spring Framework para responder HTTP
import org.springframework.http.ResponseEntity;
// Importaciones de anotaciones de controladores Spring Web
import org.springframework.web.bind.annotation.*;

// Marca la clase como un Controlador REST de Spring (devuelve JSON por defecto)
@RestController
// Define la ruta base HTTP para todos los endpoints en esta clase
@RequestMapping("/api/v1/auth")
// Lombok genera automÃ¡ticamente un constructor para inyectar los atributos 'final'
@RequiredArgsConstructor
public class AuthController {

    // Dependencia inyectada del caso de uso (Puerto de entrada). Es 'final' para garantizar inmutabilidad.
    private final RegisterUserUseCase registerUserUseCase;

    // Mapea las peticiones HTTP POST hacia la ruta '/api/v1/auth/register/buyer'
    @PostMapping("/register/buyer")
    // MÃ©todo que recibe un JSON (RequestBody) mapeado al objeto RegisterBuyerCommand
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerBuyer(@RequestBody RegisterBuyerCommand command) {
        // Ejecuta el caso de uso pasando el comando y recibe el usuario de dominio guardado
        User registeredUser = registerUserUseCase.registerBuyer(command);
        // Convierte el Usuario de Dominio (User) a un objeto seguro para enviar por red (UserResponseDTO)
        UserResponseDTO responseDto = UserResponseDTO.fromDomain(registeredUser);
        // Retorna un cÃ³digo HTTP 200 (OK) encapsulando los datos dentro del ApiResponse
        return ResponseEntity.ok(ApiResponse.ok("Buyer successfully registered", responseDto));
    }

    // Mapea las peticiones HTTP POST hacia la ruta '/api/v1/auth/register/seller'
    @PostMapping("/register/seller")
    // MÃ©todo que recibe un JSON (RequestBody) mapeado al objeto RegisterSellerCommand
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerSeller(@RequestBody RegisterSellerCommand command) {
        // Ejecuta el caso de uso pasando el comando y recibe el usuario de dominio guardado
        User registeredUser = registerUserUseCase.registerSeller(command);
        // Convierte el Usuario de Dominio (User) a un objeto seguro para enviar por red (UserResponseDTO)
        UserResponseDTO responseDto = UserResponseDTO.fromDomain(registeredUser);
        // Retorna un cÃ³digo HTTP 200 (OK) encapsulando los datos dentro del ApiResponse
        return ResponseEntity.ok(ApiResponse.ok("Seller successfully registered", responseDto));
    }
}

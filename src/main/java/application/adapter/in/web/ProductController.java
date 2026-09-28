// Paquete de Controladores Web
package application.adapter.in.web;

// Importa los DTOs de Request y Response
import application.adapter.in.web.dto.ApiResponse;
import application.adapter.in.web.dto.ProductResponseDTO;
// Importa Entidad Dominio
import application.domain.model.entity.Product;
// Importa Caso de Uso
import application.domain.ports.in.ManageProductUseCase;
// Importa Comando (DTO entrada)
import application.domain.ports.in.command.CreateProductCommand;
// AnotaciÃ³n Lombok
import lombok.RequiredArgsConstructor;
// Utilidades de HTTP Spring
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Registra la clase como un manejador de Peticiones REST
@RestController
// Asigna la ruta general del API para el catÃ¡logo
@RequestMapping("/api/v1/catalog/products")
// Inyecta dependencias
@RequiredArgsConstructor
public class ProductController {

    // Puerto de entrada para administrar catÃ¡logo
    private final ManageProductUseCase manageProductUseCase;

    // Asocia peticiones HTTP POST a la creaciÃ³n de productos
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@RequestBody CreateProductCommand command) {
        // Delega la responsabilidad de creaciÃ³n a la Capa de AplicaciÃ³n
        Product createdProduct = manageProductUseCase.createProduct(command);
        // Toma el producto puro retornado y lo limpia en un DTO para el exterior
        ProductResponseDTO responseDto = ProductResponseDTO.fromDomain(createdProduct);
        // Devuelve el cÃ³digo HTTP 200 encapsulando en el Wrapper estÃ¡ndar del proyecto
        return ResponseEntity.ok(ApiResponse.ok("Product successfully added to catalog", responseDto));
    }
}

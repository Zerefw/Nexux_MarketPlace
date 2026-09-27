// Paquete de Controladores Web
package application.adapter.in.web;

// Importa los DTOs de Request y Response
import application.adapter.in.web.dto.ApiResponse;
import application.adapter.in.web.dto.ProductResponseDTO;
// Importa Entidad Dominio
import application.domain.model.entity.Product;
// Importa Caso de Uso
import application.port.in.ManageProductUseCase;
// Importa Comando (DTO entrada)
import application.port.in.command.CreateProductCommand;
// Anotación Lombok
import lombok.RequiredArgsConstructor;
// Utilidades de HTTP Spring
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Registra la clase como un manejador de Peticiones REST
@RestController
// Asigna la ruta general del API para el catálogo
@RequestMapping("/api/v1/catalog/products")
// Inyecta dependencias
@RequiredArgsConstructor
public class ProductController {

    // Puerto de entrada para administrar catálogo
    private final ManageProductUseCase manageProductUseCase;

    // Asocia peticiones HTTP POST a la creación de productos
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@RequestBody CreateProductCommand command) {
        // Delega la responsabilidad de creación a la Capa de Aplicación
        Product createdProduct = manageProductUseCase.createProduct(command);
        // Toma el producto puro retornado y lo limpia en un DTO para el exterior
        ProductResponseDTO responseDto = ProductResponseDTO.fromDomain(createdProduct);
        // Devuelve el código HTTP 200 encapsulando en el Wrapper estándar del proyecto
        return ResponseEntity.ok(ApiResponse.ok("Product successfully added to catalog", responseDto));
    }
}

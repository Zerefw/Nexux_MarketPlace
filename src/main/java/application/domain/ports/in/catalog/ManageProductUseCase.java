// Paquete de puertos de entrada
package application.domain.ports.in;

// Importa la entidad de Dominio Product
import application.domain.model.entity.Product;
// Importa el comando de creaciÃ³n
import application.domain.ports.in.command.CreateProductCommand;

// Interfaz que define los casos de uso para administrar el catÃ¡logo
public interface ManageProductUseCase {
    
    // Firma del mÃ©todo para registrar un nuevo producto en el catÃ¡logo
    Product createProduct(CreateProductCommand command);
    
    // Firma del mÃ©todo para desactivar (borrado lÃ³gico) un producto por su ID
    void deactivateProduct(String productId);
}

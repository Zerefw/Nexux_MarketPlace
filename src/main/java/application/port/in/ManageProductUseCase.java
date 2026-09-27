// Paquete de puertos de entrada
package application.port.in;

// Importa la entidad de Dominio Product
import application.domain.model.entity.Product;
// Importa el comando de creación
import application.port.in.command.CreateProductCommand;

// Interfaz que define los casos de uso para administrar el catálogo
public interface ManageProductUseCase {
    
    // Firma del método para registrar un nuevo producto en el catálogo
    Product createProduct(CreateProductCommand command);
    
    // Firma del método para desactivar (borrado lógico) un producto por su ID
    void deactivateProduct(String productId);
}

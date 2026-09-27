// Paquete de puertos de salida
package application.port.out;

// Importa la entidad de Dominio Product
import application.domain.model.entity.Product;
// Importa el Value Object SKU
import application.domain.model.valueobject.Sku;

// Importa Optional para manejar búsquedas que pueden no retornar resultados
import java.util.Optional;

// Interfaz que abstrae las operaciones de base de datos para los Productos
public interface ProductRepositoryPort {
    
    // Guarda o actualiza un producto en la persistencia
    Product save(Product product);
    
    // Busca un producto específicamente por su ID interno
    Optional<Product> findById(String id);
    
    // Busca un producto por su código de inventario (SKU)
    Optional<Product> findBySku(Sku sku);
    
    // Verifica de forma rápida si un SKU ya existe en la base de datos
    boolean existsBySku(Sku sku);
}

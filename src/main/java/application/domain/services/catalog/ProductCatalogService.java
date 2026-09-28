// Paquete para la lÃ³gica de los Casos de Uso
package application.domain.services;

// ExcepciÃ³n base de nuestro dominio
import application.domain.exception.DomainException;
// Entidad central del catÃ¡logo
import application.domain.model.entity.Product;
// Value Objects necesarios para la creaciÃ³n
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.ProductType;
import application.domain.model.valueobject.Sku;
// Interfaz de entrada que esta clase va a implementar
import application.domain.ports.in.ManageProductUseCase;
// Comando (DTO) que trae los datos desde el exterior
import application.domain.ports.in.command.CreateProductCommand;
// Interfaz de salida para guardar los datos
import application.domain.ports.out.ProductRepositoryPort;
// Lombok para inyectar los repositorios
import lombok.RequiredArgsConstructor;
// Spring Framework: Marca la clase como Servicio
import org.springframework.stereotype.Service;
// Spring Framework: Manejo de transacciones
import org.springframework.transaction.annotation.Transactional;

// Declara este Use Case como un componente de Spring
@Service
// Lombok genera el constructor que inyecta 'productRepositoryPort'
@RequiredArgsConstructor
// Implementa las operaciones del catÃ¡logo dictadas por la interfaz
public class ProductCatalogService implements ManageProductUseCase {

    // Puerto de salida inyectado para abstraer la base de datos
    private final ProductRepositoryPort productRepositoryPort;

    // Ejecuta la lÃ³gica dentro de una transacciÃ³n SQL
    @Override
    @Transactional
    public Product createProduct(CreateProductCommand command) {
        // Convierte el string del DTO a un Value Object SKU (se auto-valida)
        Sku skuVO = new Sku(command.getSku());
        
        // Verifica que el SKU no estÃ© duplicado en el catÃ¡logo
        if (productRepositoryPort.existsBySku(skuVO)) {
            // Lanza error de dominio si hay colisiÃ³n de SKUs
            throw new DomainException("SKU is already registered in the catalog");
        }

        // Ensambla el Value Object Money para el precio
        Money priceVO = new Money(command.getPriceAmount(), command.getPriceCurrency());
        
        // Parsea de String a Enum el tipo de producto. Si falla, lanza excepciÃ³n
        ProductType typeEnum = ProductType.valueOf(command.getType().toUpperCase());

        // Llama al Factory Method de la Entidad de Dominio para crear un producto en estado vÃ¡lido
        Product product = Product.create(
                skuVO,
                command.getSellerId(),
                command.getName(),
                priceVO,
                typeEnum
        );

        // Verifica si el DTO traÃ­a una lista de variantes
        if (command.getVariants() != null) {
            // Itera cada variante (ej. "Rojo", "Talla M")
            for (String variant : command.getVariants()) {
                // Agrega la variante encapsulada a la entidad
                product.addVariant(variant);
            }
        }

        // EnvÃ­a el producto puro al puerto para ser guardado en la DB y lo retorna
        return productRepositoryPort.save(product);
    }

    // Ejecuta la desactivaciÃ³n dentro de una transacciÃ³n
    @Override
    @Transactional
    public void deactivateProduct(String productId) {
        // Busca el producto; si no existe, lanza una excepciÃ³n de dominio
        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new DomainException("Product not found"));
        
        // El producto se puede desactivar (ej. ya no se vende o estÃ¡ fuera de temporada)
        // Nota: en Dominio podrÃ­amos tener un mÃ©todo deactivate(), por ahora usamos una lÃ³gica genÃ©rica
        // Como no declaramos deactivate() en Phase 2 para Product, vamos a forzar validaciÃ³n o lÃ³gica
        // Si no existe el mÃ©todo en Phase 2, debemos agregarlo o asumir que 'active' no estÃ¡ expuesto
        // De hecho, en Product de la fase 2 no agregamos setActive(). Vamos a dejar este mÃ©todo preparado.
        throw new DomainException("Deactivation logic pending specific domain state rules.");
    }
}

// Paquete de mapeadores para la persistencia
package application.adapter.out.persistence.mapper;

// Entidad JPA
import application.adapter.out.persistence.entity.ProductJpaEntity;
// Entidad Dominio
import application.domain.model.entity.Product;
// Value Objects
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.Sku;

// Clase utilitaria sin estado
public class ProductMapper {

    // Método estático para convertir de Dominio a JPA
    public static ProductJpaEntity toJpaEntity(Product domain) {
        // Inicializa el builder de la entidad JPA
        return ProductJpaEntity.builder()
                // Copia el ID interno
                .id(domain.getId())
                // Extrae el código String desde el Value Object Sku
                .sku(domain.getSku().getCode())
                // Extrae el ID del vendedor
                .sellerId(domain.getSellerId())
                // Extrae el nombre
                .name(domain.getName())
                // Extrae la descripción
                .description(domain.getDescription())
                // Desglosa el Value Object Money extrayendo el monto numérico
                .priceAmount(domain.getPrice().getAmount())
                // Desglosa el Value Object Money extrayendo la moneda (ej. USD)
                .priceCurrency(domain.getPrice().getCurrency())
                // Extrae el Enum de tipo de producto
                .type(domain.getType())
                // Junta la lista de strings (variantes) en un solo texto separado por punto y coma (;)
                .variants(domain.getVariants() != null ? String.join(";", domain.getVariants()) : "")
                // Extrae el booleano de activación
                .active(domain.isActive())
                // Construye el objeto final
                .build();
    }

    // Método estático para convertir de JPA a Dominio (Rehidratar)
    public static Product toDomainEntity(ProductJpaEntity jpaEntity) {
        // Reconstruye el Sku VO
        Sku sku = new Sku(jpaEntity.getSku());
        // Reconstruye el Money VO
        Money price = new Money(jpaEntity.getPriceAmount(), jpaEntity.getPriceCurrency());
        
        // Reconstruye la entidad de dominio usando su Factory Method original
        Product product = Product.create(
                sku,
                jpaEntity.getSellerId(),
                jpaEntity.getName(),
                price,
                jpaEntity.getType()
        );
        
        // Separa el string almacenado de la DB por punto y coma (;)
        if (jpaEntity.getVariants() != null && !jpaEntity.getVariants().isEmpty()) {
            for (String variant : jpaEntity.getVariants().split(";")) {
                // Agrega cada variante reconstruida a la Entidad pura
                product.addVariant(variant);
            }
        }
        
        // Devuelve el objeto completamente rehidratado para uso del Use Case
        return product;
    }
}

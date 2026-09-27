// Paquete de DTOs web
package application.adapter.in.web.dto;

// Importa Entidad Pura
import application.domain.model.entity.Product;
// Anotaciones Lombok
import lombok.Builder;
import lombok.Data;
// Java Math y Listas
import java.math.BigDecimal;
import java.util.List;

// Crea getters, setters, toString, equals y hashcode
@Data
// Habilita patrón builder
@Builder
public class ProductResponseDTO {
    // Campos filtrados que el Frontend va a recibir
    private String sku;
    private String name;
    private String description;
    private BigDecimal priceAmount;
    private String priceCurrency;
    private String type;
    private List<String> variants;
    private boolean active;

    // Método fábrica para construir un DTO a partir de la Entidad de Dominio
    public static ProductResponseDTO fromDomain(Product product) {
        // Manejo de seguridad en caso de ser nulo
        if (product == null) return null;
        // Instancia usando el builder de Lombok
        return ProductResponseDTO.builder()
                .sku(product.getSku().getCode())
                .name(product.getName())
                .description(product.getDescription())
                // Despliega los campos anidados del Money VO
                .priceAmount(product.getPrice().getAmount())
                .priceCurrency(product.getPrice().getCurrency())
                .type(product.getType().name())
                .variants(product.getVariants())
                .active(product.isActive())
                .build();
    }
}

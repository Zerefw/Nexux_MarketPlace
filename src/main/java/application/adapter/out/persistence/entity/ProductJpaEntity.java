// Paquete para las entidades JPA de persistencia
package application.adapter.out.persistence.entity;

// Enum del dominio para mapear físicamente el tipo
import application.domain.model.valueobject.ProductType;
// Anotaciones de Jakarta Persistence API (JPA)
import jakarta.persistence.*;
// Anotaciones de Lombok
import lombok.*;

// Clase para manejar dinero a nivel DB (embebida)
import java.math.BigDecimal;

// Indica que es una tabla de base de datos
@Entity
// Define el nombre físico de la tabla en MySQL
@Table(name = "products")
// Genera getters automáticos
@Getter
// Genera setters automáticos
@Setter
// Constructor vacío exigido por Hibernate
@NoArgsConstructor
// Constructor con todos los campos
@AllArgsConstructor
// Patrón de construcción
@Builder
public class ProductJpaEntity {

    // Indica que es la Llave Primaria (PK)
    @Id
    // Pide a Hibernate generar un UUID automáticamente
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Define columna única y no nula para el SKU
    @Column(nullable = false, unique = true)
    private String sku;

    // Relación lógica con el Vendedor
    @Column(nullable = false)
    private String sellerId;

    // Nombre comercial del producto
    @Column(nullable = false)
    private String name;

    // Descripción comercial
    private String description;

    // Desglosa el MoneyVO: Monto del precio
    @Column(nullable = false)
    private BigDecimal priceAmount;

    // Desglosa el MoneyVO: Moneda del precio
    @Column(nullable = false)
    private String priceCurrency;

    // Guarda el Enum como un String en la BD (PHYSICAL, DIGITAL)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    // Guarda las variantes concatenadas (ej. "Rojo;Azul") en un campo de texto largo
    @Column(columnDefinition = "TEXT")
    private String variants;

    // Bandera lógica para saber si se muestra en la tienda
    @Column(nullable = false)
    private boolean active;
}

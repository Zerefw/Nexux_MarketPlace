package application.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA para almacenar los registros de inventario por bodega y SKU.
 */
@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String warehouseId;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int physicalQuantity;

    @Column(nullable = false)
    private int reservedQuantity;

    @Column(nullable = false)
    private int damagedQuantity;

    @Column(nullable = false)
    private String aisle;

    @Column(nullable = false)
    private String shelf;

    @Column(nullable = false)
    private String level;
}

package application.adapter.out.persistence.entity;

import application.domain.model.valueobject.WarehouseType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA para la tabla de bodegas fisicas.
 */
@Entity
@Table(name = "warehouses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String locationAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarehouseType type;

    @Column(nullable = false)
    private boolean active;
}

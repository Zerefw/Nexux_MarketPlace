package application.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buyer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyerProfileJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String primaryAddress;

    @Column(columnDefinition = "TEXT")
    private String secondaryAddresses; // Store as JSON or comma-separated for simplicity in this milestone

    @Column(nullable = false)
    private boolean commercialStateActive;
}

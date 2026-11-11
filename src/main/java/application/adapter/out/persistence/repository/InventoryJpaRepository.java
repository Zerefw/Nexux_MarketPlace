package application.adapter.out.persistence.repository;

import application.adapter.out.persistence.entity.InventoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para InventoryJpaEntity.
 */
@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryJpaEntity, String> {
    Optional<InventoryJpaEntity> findByWarehouseIdAndSku(String warehouseId, String sku);
    List<InventoryJpaEntity> findBySku(String sku);
}

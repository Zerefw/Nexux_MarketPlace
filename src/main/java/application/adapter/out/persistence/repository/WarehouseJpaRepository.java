package application.adapter.out.persistence.repository;

import application.adapter.out.persistence.entity.WarehouseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para WarehouseJpaEntity.
 */
@Repository
public interface WarehouseJpaRepository extends JpaRepository<WarehouseJpaEntity, String> {
    List<WarehouseJpaEntity> findByActiveTrue();
}

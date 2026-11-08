package application.adapter.out.persistence.repository;

import application.adapter.out.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad ProductJpaEntity.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, String> {

    Optional<ProductJpaEntity> findBySku(String sku);

    boolean existsBySku(String sku);

    List<ProductJpaEntity> findByActiveTrue();
}

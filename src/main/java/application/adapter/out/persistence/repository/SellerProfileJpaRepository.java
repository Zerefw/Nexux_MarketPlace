package application.adapter.out.persistence.repository;

import application.adapter.out.persistence.entity.SellerProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerProfileJpaRepository extends JpaRepository<SellerProfileJpaEntity, Long> {
    Optional<SellerProfileJpaEntity> findByUserId(String userId);
}

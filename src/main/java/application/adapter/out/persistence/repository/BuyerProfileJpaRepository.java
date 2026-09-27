package application.adapter.out.persistence.repository;

import application.adapter.out.persistence.entity.BuyerProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerProfileJpaRepository extends JpaRepository<BuyerProfileJpaEntity, Long> {
    Optional<BuyerProfileJpaEntity> findByUserId(String userId);
}

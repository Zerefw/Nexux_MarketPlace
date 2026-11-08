package application.adapter.out.persistence;

import application.adapter.out.persistence.entity.ProductJpaEntity;
import application.adapter.out.persistence.mapper.ProductMapper;
import application.adapter.out.persistence.repository.ProductJpaRepository;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Sku;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida de persistencia para los productos del catalogo.
 * Implementa ProductRepositoryPort usando Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        ProductJpaEntity jpaEntity = ProductMapper.toJpaEntity(product);
        ProductJpaEntity saved = productJpaRepository.save(jpaEntity);
        return ProductMapper.toDomainEntity(saved);
    }

    @Override
    public Optional<Product> findById(String id) {
        return productJpaRepository.findById(id).map(ProductMapper::toDomainEntity);
    }

    @Override
    public Optional<Product> findBySku(Sku sku) {
        return productJpaRepository.findBySku(sku.getCode()).map(ProductMapper::toDomainEntity);
    }

    @Override
    public boolean existsBySku(Sku sku) {
        return productJpaRepository.existsBySku(sku.getCode());
    }

    @Override
    public List<Product> findAllActive() {
        return productJpaRepository.findByActiveTrue().stream()
                .map(ProductMapper::toDomainEntity)
                .toList();
    }
}

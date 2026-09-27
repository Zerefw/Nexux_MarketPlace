// Paquete de implementaciones de adaptadores salientes
package application.adapter.out.persistence;

// Mapeador del producto
import application.adapter.out.persistence.mapper.ProductMapper;
// Entidad JPA
import application.adapter.out.persistence.entity.ProductJpaEntity;
// Repositorio Spring Data
import application.adapter.out.persistence.repository.ProductJpaRepository;
// Entidad Dominio
import application.domain.model.entity.Product;
// Value Object
import application.domain.model.valueobject.Sku;
// Puerto a implementar
import application.port.out.ProductRepositoryPort;
// Lombok
import lombok.RequiredArgsConstructor;
// Anotación de Spring
import org.springframework.stereotype.Component;

import java.util.Optional;

// Marca la clase como un Bean gestionado por Spring
@Component
// Constructor automático para dependencias finales
@RequiredArgsConstructor
// Cumple con el contrato exigido por el Application Service
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    // Dependencia al Repositorio de base de datos
    private final ProductJpaRepository productJpaRepository;

    // Guarda el producto mapeándolo a JPA
    @Override
    public Product save(Product product) {
        // Transforma de Pura a Sucia (JPA)
        ProductJpaEntity jpaEntity = ProductMapper.toJpaEntity(product);
        // Persiste en DB
        ProductJpaEntity saved = productJpaRepository.save(jpaEntity);
        // Retorna a Pura (Dominio)
        return ProductMapper.toDomainEntity(saved);
    }

    // Busca un producto por UUID nativo
    @Override
    public Optional<Product> findById(String id) {
        // Ejecuta query y mapea el resultado si existe
        return productJpaRepository.findById(id).map(ProductMapper::toDomainEntity);
    }

    // Busca un producto usando el VO Sku
    @Override
    public Optional<Product> findBySku(Sku sku) {
        // Ejecuta query extrayendo el String del SKU
        return productJpaRepository.findBySku(sku.getCode()).map(ProductMapper::toDomainEntity);
    }

    // Valida la existencia de un SKU rápidamente
    @Override
    public boolean existsBySku(Sku sku) {
        // Usa método optimizado del repositorio Spring Data
        return productJpaRepository.existsBySku(sku.getCode());
    }
}

// Paquete de Repositorios JPA
package application.adapter.out.persistence.repository;

// Importa la entidad JPA recién creada
import application.adapter.out.persistence.entity.ProductJpaEntity;
// Importa la interfaz JpaRepository de Spring Data
import org.springframework.data.jpa.repository.JpaRepository;
// Anotación de Spring para registrar el Bean del repositorio
import org.springframework.stereotype.Repository;

// Utilidad Optional
import java.util.Optional;

// Spring Data JPA proveerá la implementación automática de esta interfaz
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, String> {
    
    // Spring genera la query: SELECT * FROM products WHERE sku = ?
    Optional<ProductJpaEntity> findBySku(String sku);
    
    // Spring genera la query: SELECT count(id)>0 FROM products WHERE sku = ?
    boolean existsBySku(String sku);
}

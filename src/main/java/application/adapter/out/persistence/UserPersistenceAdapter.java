// Paquete del adaptador de persistencia (Infraestructura)
package application.adapter.out.persistence;

// Importa la entidad de Spring Data JPA ligada a la tabla de base de datos
import application.adapter.out.persistence.entity.UserJpaEntity;
// Importa la clase utilitaria que transfiere datos entre Entidad de Dominio y Entidad JPA
import application.adapter.out.persistence.mapper.UserMapper;
// Importa la interfaz Repository de Spring Data JPA
import application.adapter.out.persistence.repository.UserJpaRepository;
// Importa la Entidad de Dominio real
import application.domain.model.entity.User;
// Importa el Value Object de Dominio
import application.domain.model.valueobject.Email;
// Importa la interfaz/puerto del dominio que este adaptador debe cumplir
import application.port.out.UserRepositoryPort;
// Lombok para crear un constructor que inyecte dependencias finales
import lombok.RequiredArgsConstructor;
// Marca la clase como componente manejado por Spring
import org.springframework.stereotype.Component;

// Importación para envoltorios opcionales de valores nulos
import java.util.Optional;

// Spring registra automáticamente este Adapter en el contexto de Beans
@Component
// Inyección por constructor de las dependencias 'final' (en este caso el JpaRepository)
@RequiredArgsConstructor
// Implementa el puerto UserRepositoryPort que el Dominio exige
public class UserPersistenceAdapter implements UserRepositoryPort {

    // Dependencia de la interfaz Spring Data JPA para realizar queries a MySQL
    private final UserJpaRepository userRepository;

    // Sobrescribe el método save() ordenado por el dominio
    @Override
    public User save(User user, String encodedPassword) {
        // 1. Convierte el modelo de Dominio puro a un modelo "Sucio" de Base de Datos (JPA Entity)
        UserJpaEntity jpaEntity = UserMapper.toJpaEntity(user, encodedPassword);
        // 2. Llama al método 'save' nativo de Spring Data JPA para hacer el INSERT en la base de datos
        UserJpaEntity savedEntity = userRepository.save(jpaEntity);
        // 3. Vuelve a mapear la Entidad JPA (que ahora tiene un ID autogenerado) a Entidad de Dominio y la retorna
        return UserMapper.toDomainEntity(savedEntity);
    }

    // Sobrescribe la búsqueda por Email
    @Override
    public Optional<User> findByEmail(Email email) {
        // Consulta la base de datos usando el String extraído del Value Object
        return userRepository.findByEmail(email.getAddress())
                // Transforma el Optional<UserJpaEntity> resultante a un Optional<User> de Dominio usando el Mapper
                .map(UserMapper::toDomainEntity);
    }

    // Sobrescribe el chequeo de existencia por email
    @Override
    public boolean existsByEmail(Email email) {
        // Ejecuta query directa de conteo/existencia usando Spring Data
        return userRepository.existsByEmail(email.getAddress());
    }

    // Sobrescribe el chequeo de existencia por documento de identidad
    @Override
    public boolean existsByIdentificationDocument(String identificationDocument) {
        // Llama al método autogenerado de la interfaz JPA para validar existencia rápida en MySQL
        return userRepository.existsByIdentificationDocument(identificationDocument);
    }
}

package application.adapter.out.persistence;

import application.adapter.out.persistence.entity.UserJpaEntity;
import application.adapter.out.persistence.mapper.UserMapper;
import application.adapter.out.persistence.repository.UserJpaRepository;
import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;
import application.domain.ports.out.user.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de salida de persistencia para la entidad User.
 * Implementa el puerto UserRepositoryPort utilizando Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;

    @Override
    public User save(User user, String encodedPassword) {
        UserJpaEntity jpaEntity = UserMapper.toJpaEntity(user, encodedPassword);
        UserJpaEntity savedEntity = userRepository.save(jpaEntity);
        return UserMapper.toDomainEntity(savedEntity);
    }

    @Override
    public User save(User user) {
        String existingPassword = userRepository.findById(user.getId())
                .map(UserJpaEntity::getPassword)
                .orElse("");
        UserJpaEntity jpaEntity = UserMapper.toJpaEntity(user, existingPassword);
        UserJpaEntity savedEntity = userRepository.save(jpaEntity);
        return UserMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id)
                .map(UserMapper::toDomainEntity);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userRepository.findByEmail(email.getAddress())
                .map(UserMapper::toDomainEntity);
    }

    @Override
    public Optional<String> findEncodedPasswordByEmail(Email email) {
        return userRepository.findByEmail(email.getAddress())
                .map(UserJpaEntity::getPassword);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userRepository.existsByEmail(email.getAddress());
    }

    @Override
    public boolean existsByIdentificationDocument(String identificationDocument) {
        return userRepository.existsByIdentificationDocument(identificationDocument);
    }
}

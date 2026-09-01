package application.port.out;

import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user, String encodedPassword);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    boolean existsByIdentificationDocument(String identificationDocument);
}

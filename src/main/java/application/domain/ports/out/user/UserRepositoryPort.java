package application.domain.ports.out.user;

import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;

import java.util.Optional;

/**
 * Puerto de salida para gestionar la persistencia de la entidad User.
 */
public interface UserRepositoryPort {

    /**
     * Persiste un usuario junto con su contrasena codificada.
     *
     * @param user            Entidad de dominio User.
     * @param encodedPassword Contrasena cifrada.
     * @return Usuario persistido con su identificador asignado.
     */
    User save(User user, String encodedPassword);

    /**
     * Actualiza el estado de un usuario existente en la persistencia.
     *
     * @param user Entidad de dominio User modificada.
     * @return Usuario actualizado.
     */
    User save(User user);

    /**
     * Busca un usuario por su identificador unico.
     *
     * @param id Identificador del usuario.
     * @return Optional con el usuario encontrado o vacio.
     */
    Optional<User> findById(String id);

    /**
     * Busca un usuario por su direccion de correo electronico.
     *
     * @param email Objeto de valor Email.
     * @return Optional con el usuario encontrado o vacio.
     */
    Optional<User> findByEmail(Email email);

    /**
     * Recupera el hash de contrasena almacenado para un correo electronico.
     *
     * @param email Objeto de valor Email.
     * @return Optional con el hash de contrasena.
     */
    Optional<String> findEncodedPasswordByEmail(Email email);

    /**
     * Verifica si ya existe un usuario registrado con el correo electronico indicado.
     *
     * @param email Objeto de valor Email.
     * @return true si el correo ya existe, false en caso contrario.
     */
    boolean existsByEmail(Email email);

    /**
     * Verifica si ya existe un usuario con el documento de identificacion dado.
     *
     * @param identificationDocument Documento de identidad.
     * @return true si el documento ya existe, false en caso contrario.
     */
    boolean existsByIdentificationDocument(String identificationDocument);
}

package application.domain.ports.out.user;

/**
 * Puerto de salida para el cifrado y verificacion criptografica de contrasenas.
 */
public interface PasswordEncoderPort {

    /**
     * Codifica una contrasena en texto plano.
     *
     * @param rawPassword Contrasena sin cifrar.
     * @return Contrasena cifrada.
     */
    String encode(String rawPassword);

    /**
     * Verifica si una contrasena en texto plano coincide con su hash cifrado.
     *
     * @param rawPassword     Contrasena en texto plano.
     * @param encodedPassword Hash cifrado almacenado.
     * @return true si coinciden, false en caso contrario.
     */
    boolean matches(String rawPassword, String encodedPassword);
}

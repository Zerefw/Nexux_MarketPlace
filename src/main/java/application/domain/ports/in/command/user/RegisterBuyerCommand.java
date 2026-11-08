package application.domain.ports.in.command.user;

/**
 * Comando para registrar un nuevo usuario con rol de comprador.
 *
 * @param identificationDocument Documento nacional de identificacion.
 * @param fullName               Nombre completo del comprador.
 * @param email                  Correo electronico del comprador.
 * @param rawPassword            Contrasena en texto plano.
 * @param primaryAddress         Direccion principal de entrega.
 */
public record RegisterBuyerCommand(
        String identificationDocument,
        String fullName,
        String email,
        String rawPassword,
        String primaryAddress
) {
    public String getIdentificationDocument() { return identificationDocument; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRawPassword() { return rawPassword; }
    public String getPrimaryAddress() { return primaryAddress; }
}

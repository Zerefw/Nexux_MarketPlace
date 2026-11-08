package application.domain.ports.in.command.user;

/**
 * Comando para registrar un nuevo usuario con rol de vendedor.
 *
 * @param identificationDocument Documento nacional de identificacion.
 * @param fullName               Nombre completo del vendedor.
 * @param email                  Correo electronico de contacto y acceso.
 * @param rawPassword            Contrasena en texto plano.
 * @param storeName              Nombre comercial de la tienda.
 * @param taxId                  Identificador tributario o fiscal.
 */
public record RegisterSellerCommand(
        String identificationDocument,
        String fullName,
        String email,
        String rawPassword,
        String storeName,
        String taxId
) {
    public String getIdentificationDocument() { return identificationDocument; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRawPassword() { return rawPassword; }
    public String getStoreName() { return storeName; }
    public String getTaxId() { return taxId; }
}

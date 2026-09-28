// Paquete de Casos de Uso (Capa de AplicaciÃ³n)
package application.domain.services;

// Importa la excepciÃ³n personalizada de dominio para manejar errores de negocio
import application.domain.exception.DomainException;
// Importa las entidades puras del dominio
import application.domain.model.entity.BuyerProfile;
import application.domain.model.entity.SellerProfile;
import application.domain.model.entity.User;
// Importa Value Objects y Enumeradores
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
// Importa el puerto de entrada que esta clase va a implementar
import application.domain.ports.in.RegisterUserUseCase;
// Importa los comandos (DTOs) que recibe desde el controlador
import application.domain.ports.in.command.RegisterBuyerCommand;
import application.domain.ports.in.command.RegisterSellerCommand;
// Importa los puertos de salida (Repositorios y Encriptador) que requiere para trabajar
import application.domain.ports.out.BuyerProfileRepositoryPort;
import application.domain.ports.out.PasswordEncoderPort;
import application.domain.ports.out.SellerProfileRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
// AnotaciÃ³n Lombok para crear el constructor de las dependencias automÃ¡ticamente
import lombok.RequiredArgsConstructor;
// AnotaciÃ³n de Spring para registrar la clase como un servicio gestionado (Bean)
import org.springframework.stereotype.Service;
// AnotaciÃ³n de Spring para asegurar que los mÃ©todos operen dentro de una transacciÃ³n de base de datos
import org.springframework.transaction.annotation.Transactional;

// Registra el componente en el contenedor de Spring
@Service
// Lombok inyecta las variables finales mediante el constructor
@RequiredArgsConstructor
// Implementa el puerto de entrada RegisterUserUseCase
public class RegisterUserService implements RegisterUserUseCase {

    // Puerto de salida para interactuar con la persistencia de Usuarios
    private final UserRepositoryPort userRepositoryPort;
    // Puerto de salida para interactuar con la persistencia de Perfiles de Comprador
    private final BuyerProfileRepositoryPort buyerProfileRepositoryPort;
    // Puerto de salida para interactuar con la persistencia de Perfiles de Vendedor
    private final SellerProfileRepositoryPort sellerProfileRepositoryPort;
    // Puerto de salida para encriptar la contraseÃ±a (sin atarse directamente a Spring Security aquÃ­)
    private final PasswordEncoderPort passwordEncoderPort;

    // Sobrescribe el mÃ©todo definido en el puerto de entrada para compradores
    @Override
    // Marca el mÃ©todo como transaccional: si falla un paso (ej. guardar perfil), se hace rollback del usuario
    @Transactional
    public User registerBuyer(RegisterBuyerCommand command) {
        // Crea el Value Object Email para asegurar que tenga un formato vÃ¡lido inmediatamente
        Email emailVO = new Email(command.getEmail());
        // Llama al mÃ©todo privado para validar que el email o documento no existan previamente
        validateUserUniqueness(emailVO, command.getIdentificationDocument());

        // Llama al mÃ©todo de fÃ¡brica de la entidad de Dominio para crear un nuevo Usuario
        User user = User.create(
                command.getIdentificationDocument(), // Pasa el documento del comando
                command.getFullName(),               // Pasa el nombre completo
                emailVO,                             // Pasa el Email validado
                UserRole.BUYER                       // Fija el rol estricto como COMPRADOR
        );

        // Encripta la contraseÃ±a en texto plano utilizando el puerto de seguridad
        String encodedPassword = passwordEncoderPort.encode(command.getRawPassword());
        // Guarda el usuario en base de datos mandando la entidad y el password encriptado
        User savedUser = userRepositoryPort.save(user, encodedPassword);

        // Crea la entidad de dominio de Perfil de Comprador usando el ID autogenerado del usuario
        BuyerProfile profile = BuyerProfile.create(savedUser.getId(), command.getPrimaryAddress());
        // Guarda el perfil del comprador a travÃ©s de su respectivo puerto de salida
        buyerProfileRepositoryPort.save(profile);

        // Retorna la entidad del usuario reciÃ©n creada
        return savedUser;
    }

    // Sobrescribe el mÃ©todo definido en el puerto de entrada para vendedores
    @Override
    // Garantiza transaccionalidad atÃ³mica (todo o nada) en la base de datos
    @Transactional
    public User registerSeller(RegisterSellerCommand command) {
        // Crea y valida el Value Object del Email
        Email emailVO = new Email(command.getEmail());
        // Revisa reglas de unicidad en la base de datos para no tener duplicados
        validateUserUniqueness(emailVO, command.getIdentificationDocument());

        // Crea el objeto User puro del dominio
        User user = User.create(
                command.getIdentificationDocument(), // Documento de identificaciÃ³n
                command.getFullName(),               // Nombre del vendedor
                emailVO,                             // Email
                UserRole.SELLER                      // Fija el rol estricto como VENDEDOR
        );

        // Encripta la contraseÃ±a
        String encodedPassword = passwordEncoderPort.encode(command.getRawPassword());
        // Persiste el usuario y recupera el objeto actualizado con su ID generado
        User savedUser = userRepositoryPort.save(user, encodedPassword);

        // Genera el perfil de vendedor asociÃ¡ndole los datos comerciales de su tienda
        SellerProfile profile = SellerProfile.create(
                savedUser.getId(),       // ID del usuario padre
                command.getStoreName(),  // Nombre de la tienda
                command.getTaxId(),      // Identificador de impuestos/RUT
                emailVO                  // Correo de contacto
        );
        // Persiste el perfil en la base de datos
        sellerProfileRepositoryPort.save(profile);

        // Devuelve el usuario persistido
        return savedUser;
    }

    // MÃ©todo privado interno de validaciÃ³n
    private void validateUserUniqueness(Email email, String identificationDocument) {
        // Consulta si el email ya existe en el puerto (base de datos)
        if (userRepositoryPort.existsByEmail(email)) {
            // Lanza excepciÃ³n de negocio si se viola la unicidad del correo
            throw new DomainException("Email is already registered");
        }
        // Consulta si el documento ya existe en el puerto
        if (userRepositoryPort.existsByIdentificationDocument(identificationDocument)) {
            // Lanza excepciÃ³n de negocio si el documento de identidad estÃ¡ repetido
            throw new DomainException("Identification document is already registered");
        }
    }
}

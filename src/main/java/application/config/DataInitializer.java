package application.config;

import application.domain.model.entity.User;
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
import application.domain.ports.out.user.PasswordEncoderPort;
import application.domain.ports.out.user.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos maestros que asegura la existencia de un usuario Administrador
 * y un Operador Logistico iniciales para pruebas y ejecucion de flujos privilegiados.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public void run(String... args) {
        inicializarAdministrador();
        inicializarOperadorLogistico();
    }

    private void inicializarAdministrador() {
        Email adminEmail = new Email("admin@nexus.com");
        if (!userRepositoryPort.existsByEmail(adminEmail)) {
            User admin = User.create(
                    "ADMIN-0001",
                    "Administrador Nexus",
                    adminEmail,
                    UserRole.ADMIN
            );
            userRepositoryPort.save(admin, passwordEncoderPort.encode("Admin123!"));
            log.info("Usuario Administrador inicializado exitosamente: admin@nexus.com / Admin123!");
        }
    }

    private void inicializarOperadorLogistico() {
        Email logisticsEmail = new Email("logistics@nexus.com");
        if (!userRepositoryPort.existsByEmail(logisticsEmail)) {
            User logistics = User.create(
                    "LOGISTICS-0001",
                    "Operador Logistico Nexus",
                    logisticsEmail,
                    UserRole.LOGISTICS_OPERATOR
            );
            userRepositoryPort.save(logistics, passwordEncoderPort.encode("Logistics123!"));
            log.info("Usuario Operador Logistico inicializado exitosamente: logistics@nexus.com / Logistics123!");
        }
    }
}

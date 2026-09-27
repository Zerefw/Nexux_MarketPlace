// Paquete de configuración del proyecto
package application.config;

// Importa anotación para exponer Beans de Spring
import org.springframework.context.annotation.Bean;
// Importa anotación para marcar como clase de configuración
import org.springframework.context.annotation.Configuration;
// Importa configuración HTTP de Spring Security
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Habilita las protecciones de seguridad web globales
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// Proveedor concreto del algoritmo BCrypt para contraseñas seguras
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// Interfaz base de encriptación de Spring Security
import org.springframework.security.crypto.password.PasswordEncoder;
// Representa la cadena de filtros de seguridad aplicada a la app
import org.springframework.security.web.SecurityFilterChain;
// Enumerador para políticas de creación de sesiones (ej. STATELESS)
import org.springframework.security.config.http.SessionCreationPolicy;

// Indica que es un componente de Spring que genera Beans
@Configuration
// Aplica filtros de seguridad por defecto sobre todas las peticiones
@EnableWebSecurity
public class SecurityConfig {

    // Registra este método como Bean para sobrescribir la configuración de seguridad predeterminada
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Desactiva la protección CSRF, requerida típicamente en APIs REST y clientes móviles
        http.csrf(csrf -> csrf.disable())
            // Define política de manejo de sesiones para evitar almacenar estado en el servidor (útil para JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Comienza la definición de reglas de autorización para rutas HTTP
            .authorizeHttpRequests(auth -> auth
                // Autoriza el acceso libre (sin token) a todas las rutas que comiencen con /api/v1/auth/
                .requestMatchers("/api/v1/auth/**").permitAll()
                // Indica que absolutamente cualquier otra ruta debe requerir autenticación
                .anyRequest().authenticated()
            );
        // Construye y retorna la cadena de filtros con nuestras personalizaciones
        return http.build();
    }
    
    // Declara el bean PasswordEncoder para que pueda ser inyectado (por ejemplo, en nuestro Adapter)
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorna una instancia del algoritmo estándar de industria BCrypt, con la fuerza predeterminada
        return new BCryptPasswordEncoder();
    }
}

package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Usuario dentro del Marketplace.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private String id;
    private String identificationDocument;
    private String fullName;
    private Email email;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;

    /**
     * Crea un nuevo usuario validando todos sus campos obligatorios.
     */
    public static User create(String identificationDocument, String fullName, Email email, UserRole role) {
        if (identificationDocument == null || identificationDocument.isBlank()) {
            throw new DomainException("El documento de identidad es requerido");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new DomainException("El nombre completo es requerido");
        }
        if (email == null) {
            throw new DomainException("El correo electronico es requerido");
        }
        if (role == null) {
            throw new DomainException("El rol de usuario es requerido");
        }

        return User.builder()
                .identificationDocument(identificationDocument)
                .fullName(fullName)
                .email(email)
                .role(role)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Suspende al usuario.
     */
    public void suspend() {
        this.active = false;
    }

    /**
     * Activa al usuario.
     */
    public void activate() {
        this.active = true;
    }
}

package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Email;
import application.domain.model.valueobject.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private String id; // UUID or String based for MongoDB/MySQL mix
    private String identificationDocument;
    private String fullName;
    private Email email;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;

    public static User create(String identificationDocument, String fullName, Email email, UserRole role) {
        if (identificationDocument == null || identificationDocument.isBlank()) throw new DomainException("ID Document is required");
        
        return User.builder()
                .identificationDocument(identificationDocument)
                .fullName(fullName)
                .email(email)
                .role(role)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void suspend() { this.active = false; }
    public void activate() { this.active = true; }
}

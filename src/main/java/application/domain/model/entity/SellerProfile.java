package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Perfil de vendedor.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SellerProfile {
    private String userId;
    private String storeName;
    private String taxId;
    private Email contactEmail;
    private boolean active;

    /**
     * Crea perfil de vendedor.
     */
    public static SellerProfile create(String userId, String storeName, String taxId, Email contactEmail) {
        if (userId == null) throw new DomainException("El ID de usuario es requerido");
        if (storeName == null || storeName.isBlank()) throw new DomainException("El nombre de tienda es requerido");

        return SellerProfile.builder()
                .userId(userId)
                .storeName(storeName)
                .taxId(taxId)
                .contactEmail(contactEmail)
                .active(true)
                .build();
    }

    /**
     * Actualiza el identificador fiscal.
     */
    public void updateTaxId(String taxId) {
        if (taxId == null || taxId.isBlank()) throw new DomainException("El identificador fiscal no puede ser nulo o vacio");
        this.taxId = taxId;
    }

    /**
     * Desactiva perfil.
     */
    public void deactivate() { this.active = false; }
}

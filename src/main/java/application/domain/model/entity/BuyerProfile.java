package application.domain.model.entity;

import application.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Perfil de comprador.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuyerProfile {
    private String userId;
    private String primaryAddress;
    @Builder.Default
    private List<String> secondaryAddresses = new ArrayList<>();
    private boolean commercialStateActive;

    /**
     * Crea perfil de comprador.
     */
    public static BuyerProfile create(String userId, String primaryAddress) {
        if (userId == null) throw new DomainException("El ID de usuario es requerido");
        if (primaryAddress == null || primaryAddress.isBlank()) throw new DomainException("Direccion principal requerida");

        return BuyerProfile.builder()
                .userId(userId)
                .primaryAddress(primaryAddress)
                .secondaryAddresses(new ArrayList<>())
                .commercialStateActive(true)
                .build();
    }

    /**
     * Anade direccion secundaria.
     */
    public void addSecondaryAddress(String address) {
        if (address != null && !address.isBlank()) {
            this.secondaryAddresses.add(address);
        }
    }
}

package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SellerProfile {
    private String userId;
    private String storeName;
    private String taxId;
    private Email contactEmail;
    private boolean active;

    public static SellerProfile create(String userId, String storeName, String taxId, Email contactEmail) {
        if (userId == null) throw new DomainException("User ID is required");
        if (storeName == null || storeName.isBlank()) throw new DomainException("Store name is required");

        return SellerProfile.builder()
                .userId(userId)
                .storeName(storeName)
                .taxId(taxId)
                .contactEmail(contactEmail)
                .active(true)
                .build();
    }

    public void deactivate() { this.active = false; }
}

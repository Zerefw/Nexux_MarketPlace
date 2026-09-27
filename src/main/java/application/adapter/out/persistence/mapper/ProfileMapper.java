package application.adapter.out.persistence.mapper;

import application.adapter.out.persistence.entity.BuyerProfileJpaEntity;
import application.adapter.out.persistence.entity.SellerProfileJpaEntity;
import application.domain.model.entity.BuyerProfile;
import application.domain.model.entity.SellerProfile;
import application.domain.model.valueobject.Email;

public class ProfileMapper {

    public static BuyerProfileJpaEntity toJpaEntity(BuyerProfile domain) {
        return BuyerProfileJpaEntity.builder()
                .userId(domain.getUserId())
                .primaryAddress(domain.getPrimaryAddress())
                .secondaryAddresses(String.join(";", domain.getSecondaryAddresses()))
                .commercialStateActive(domain.isCommercialStateActive())
                .build();
    }

    public static BuyerProfile toDomainEntity(BuyerProfileJpaEntity jpaEntity) {
        BuyerProfile profile = BuyerProfile.builder()
                .userId(jpaEntity.getUserId())
                .primaryAddress(jpaEntity.getPrimaryAddress())
                .commercialStateActive(jpaEntity.isCommercialStateActive())
                .build();
        
        if (jpaEntity.getSecondaryAddresses() != null && !jpaEntity.getSecondaryAddresses().isBlank()) {
            for (String addr : jpaEntity.getSecondaryAddresses().split(";")) {
                profile.addSecondaryAddress(addr);
            }
        }
        return profile;
    }

    public static SellerProfileJpaEntity toJpaEntity(SellerProfile domain) {
        return SellerProfileJpaEntity.builder()
                .userId(domain.getUserId())
                .storeName(domain.getStoreName())
                .taxId(domain.getTaxId())
                .contactEmail(domain.getContactEmail().getAddress())
                .active(domain.isActive())
                .build();
    }

    public static SellerProfile toDomainEntity(SellerProfileJpaEntity jpaEntity) {
        return SellerProfile.builder()
                .userId(jpaEntity.getUserId())
                .storeName(jpaEntity.getStoreName())
                .taxId(jpaEntity.getTaxId())
                .contactEmail(new Email(jpaEntity.getContactEmail()))
                .active(jpaEntity.isActive())
                .build();
    }
}

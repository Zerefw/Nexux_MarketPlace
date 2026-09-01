package application.port.out;

import application.domain.model.entity.SellerProfile;

public interface SellerProfileRepositoryPort {
    SellerProfile save(SellerProfile profile);
}

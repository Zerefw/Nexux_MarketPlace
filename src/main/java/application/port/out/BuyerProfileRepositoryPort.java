package application.port.out;

import application.domain.model.entity.BuyerProfile;

public interface BuyerProfileRepositoryPort {
    BuyerProfile save(BuyerProfile profile);
}

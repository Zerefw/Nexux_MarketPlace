package application.adapter.out.persistence;

import application.adapter.out.persistence.entity.BuyerProfileJpaEntity;
import application.adapter.out.persistence.entity.SellerProfileJpaEntity;
import application.adapter.out.persistence.mapper.ProfileMapper;
import application.adapter.out.persistence.repository.BuyerProfileJpaRepository;
import application.adapter.out.persistence.repository.SellerProfileJpaRepository;
import application.domain.model.entity.BuyerProfile;
import application.domain.model.entity.SellerProfile;
import application.domain.ports.out.BuyerProfileRepositoryPort;
import application.domain.ports.out.SellerProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements BuyerProfileRepositoryPort, SellerProfileRepositoryPort {

    private final BuyerProfileJpaRepository buyerRepo;
    private final SellerProfileJpaRepository sellerRepo;

    @Override
    public BuyerProfile save(BuyerProfile profile) {
        BuyerProfileJpaEntity entity = ProfileMapper.toJpaEntity(profile);
        BuyerProfileJpaEntity saved = buyerRepo.save(entity);
        return ProfileMapper.toDomainEntity(saved);
    }

    @Override
    public SellerProfile save(SellerProfile profile) {
        SellerProfileJpaEntity entity = ProfileMapper.toJpaEntity(profile);
        SellerProfileJpaEntity saved = sellerRepo.save(entity);
        return ProfileMapper.toDomainEntity(saved);
    }
}

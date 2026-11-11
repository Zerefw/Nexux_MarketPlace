package application.adapter.out.persistence;

import application.adapter.out.persistence.entity.WarehouseJpaEntity;
import application.adapter.out.persistence.repository.WarehouseJpaRepository;
import application.domain.model.entity.Warehouse;
import application.domain.ports.out.warehouse.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida de persistencia para la entidad Warehouse.
 */
@Component
@RequiredArgsConstructor
public class WarehousePersistenceAdapter implements WarehouseRepositoryPort {

    private final WarehouseJpaRepository warehouseJpaRepository;

    @Override
    public Warehouse save(Warehouse warehouse) {
        WarehouseJpaEntity jpa = WarehouseJpaEntity.builder()
                .id(warehouse.getId())
                .ownerId(warehouse.getOwnerId())
                .name(warehouse.getName())
                .locationAddress(warehouse.getLocationAddress())
                .type(warehouse.getType())
                .active(warehouse.isActive())
                .build();
        WarehouseJpaEntity saved = warehouseJpaRepository.save(jpa);
        return toDomain(saved);
    }

    @Override
    public Optional<Warehouse> findById(String id) {
        return warehouseJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Warehouse> findAllActive() {
        return warehouseJpaRepository.findByActiveTrue().stream()
                .map(this::toDomain)
                .toList();
    }

    private Warehouse toDomain(WarehouseJpaEntity jpa) {
        return Warehouse.builder()
                .id(jpa.getId())
                .ownerId(jpa.getOwnerId())
                .name(jpa.getName())
                .locationAddress(jpa.getLocationAddress())
                .type(jpa.getType())
                .active(jpa.isActive())
                .build();
    }
}

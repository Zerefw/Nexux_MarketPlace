package application.adapter.out.persistence.mapper;

import application.adapter.out.persistence.entity.ProductJpaEntity;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.Sku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Mapeador bidireccional entre la entidad de dominio Product y la entidad JPA ProductJpaEntity.
 */
public class ProductMapper {

    /**
     * Convierte una entidad de dominio Product en su representacion JPA.
     *
     * @param domain Entidad de dominio Product.
     * @return Entidad ProductJpaEntity lista para persistir.
     */
    public static ProductJpaEntity toJpaEntity(Product domain) {
        return ProductJpaEntity.builder()
                .id(domain.getId())
                .sku(domain.getSku().getCode())
                .sellerId(domain.getSellerId())
                .name(domain.getName())
                .description(domain.getDescription())
                .priceAmount(domain.getPrice().getAmount())
                .priceCurrency(domain.getPrice().getCurrency())
                .type(domain.getType())
                .variants(domain.getVariants() != null ? String.join(";", domain.getVariants()) : "")
                .active(domain.isActive())
                .build();
    }

    /**
     * Reconstruye la entidad de dominio Product preservando su ID, descripcion, variantes y estado activo.
     *
     * @param jpaEntity Entidad recuperada desde la base de datos.
     * @return Entidad de dominio Product completamente hidratada.
     */
    public static Product toDomainEntity(ProductJpaEntity jpaEntity) {
        Sku sku = new Sku(jpaEntity.getSku());
        Money price = new Money(jpaEntity.getPriceAmount(), jpaEntity.getPriceCurrency());

        List<String> variants = new ArrayList<>();
        if (jpaEntity.getVariants() != null && !jpaEntity.getVariants().isBlank()) {
            variants.addAll(Arrays.asList(jpaEntity.getVariants().split(";")));
        }

        return Product.builder()
                .id(jpaEntity.getId())
                .sku(sku)
                .sellerId(jpaEntity.getSellerId())
                .name(jpaEntity.getName())
                .description(jpaEntity.getDescription())
                .price(price)
                .type(jpaEntity.getType())
                .variants(variants)
                .active(jpaEntity.isActive())
                .build();
    }
}

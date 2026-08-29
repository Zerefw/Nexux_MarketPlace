package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.ProductType;
import application.domain.model.valueobject.Sku;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {
    private String id;
    private Sku sku;
    private String sellerId;
    private String name;
    private String description;
    private Money price;
    private ProductType type;
    @Builder.Default
    private List<String> variants = new ArrayList<>();
    private boolean active;

    public static Product create(Sku sku, String sellerId, String name, Money price, ProductType type) {
        if (sku == null || sellerId == null || name == null || price == null || type == null) {
            throw new DomainException("Required fields are missing");
        }
        return Product.builder()
                .sku(sku)
                .sellerId(sellerId)
                .name(name)
                .price(price)
                .type(type)
                .variants(new ArrayList<>())
                .active(true)
                .build();
    }

    public void addVariant(String variant) {
        if (variant != null && !variant.isBlank()) {
            this.variants.add(variant);
        }
    }
}

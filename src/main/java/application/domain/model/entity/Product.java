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

/**
 * Entidad que representa un producto en el sistema.
 */
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

    /**
     * Crea un nuevo producto.
     */
    public static Product create(Sku sku, String sellerId, String name, Money price, ProductType type) {
        if (sku == null || sellerId == null || name == null || price == null || type == null) {
            throw new DomainException("Faltan campos requeridos");
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

    /**
     * Anade una variante al producto.
     */
    public void addVariant(String variant) {
        if (variant != null && !variant.isBlank()) {
            this.variants.add(variant);
        }
    }
    
    /**
     * Actualiza los detalles del producto.
     */
    public void updateDetails(String description, Money price) {
        this.description = description;
        this.price = price;
    }
    
    /**
     * Desactiva el producto.
     */
    public void deactivate() {
        this.active = false;
    }
}

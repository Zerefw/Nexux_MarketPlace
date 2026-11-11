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
 * Entidad que representa un producto comercializable en el catalogo.
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
     * Crea un nuevo producto validando sus campos requeridos.
     */
    public static Product create(Sku sku, String sellerId, String name, Money price, ProductType type) {
        if (sku == null || sellerId == null || sellerId.isBlank() || name == null || name.isBlank() || price == null || type == null) {
            throw new DomainException("Faltan campos requeridos para crear el producto");
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
     * Anade una variante al producto validando que no sea vacia.
     */
    public void addVariant(String variant) {
        if (variant == null || variant.isBlank()) {
            throw new DomainException("La variante del producto no puede ser nula ni vacia");
        }
        this.variants.add(variant.trim());
    }

    /**
     * Actualiza los detalles descriptivos y el precio del producto.
     */
    public void updateDetails(String description, Money price) {
        if (description == null || description.isBlank()) {
            throw new DomainException("La descripcion del producto es obligatoria");
        }
        if (price == null) {
            throw new DomainException("El precio del producto es obligatorio");
        }
        this.description = description;
        this.price = price;
    }

    /**
     * Desactiva el producto logicamente.
     */
    public void deactivate() {
        this.active = false;
    }
}

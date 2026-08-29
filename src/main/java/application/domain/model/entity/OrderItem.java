package application.domain.model.entity;

import application.domain.exception.DomainException;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.Sku;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {
    private String id;
    private Sku sku;
    private int quantity;
    private Money unitPrice;
    private Money subTotal;

    public static OrderItem create(Sku sku, int quantity, Money unitPrice) {
        if (quantity <= 0) throw new DomainException("Quantity must be positive");
        return OrderItem.builder()
                .sku(sku)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subTotal(unitPrice.multiply(quantity))
                .build();
    }
}

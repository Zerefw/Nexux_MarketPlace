package application.domain.model.aggregate;

import application.domain.exception.DomainException;
import application.domain.model.entity.OrderItem;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.OrderStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {
    private String id;
    private String buyerId;
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status;
    private Money totalAmount;
    private LocalDateTime createdAt;
    private String shippingAddress;

    public static Order createCart(String buyerId, String currency) {
        if (buyerId == null) throw new DomainException("Buyer ID is required");

        return Order.builder()
                .buyerId(buyerId)
                .status(OrderStatus.CART)
                .createdAt(LocalDateTime.now())
                .totalAmount(Money.zero(currency))
                .items(new ArrayList<>())
                .build();
    }

    public void setShippingAddress(String address) {
        this.shippingAddress = address;
    }

    public void addItem(OrderItem item) {
        if (this.status != OrderStatus.CART) throw new DomainException("Items can only be added to CART");
        this.items.add(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        if (items.isEmpty()) return;
        String currency = items.get(0).getUnitPrice().getCurrency();
        Money total = Money.zero(currency);
        for (OrderItem item : items) {
            total = total.add(item.getSubTotal());
        }
        this.totalAmount = total;
    }

    public void checkout() {
        if (this.status != OrderStatus.CART) throw new DomainException("Only CART can checkout");
        if (this.shippingAddress == null || this.shippingAddress.isBlank()) {
            throw new DomainException("Shipping address is required for checkout");
        }
        if (this.items.isEmpty()) throw new DomainException("Cannot checkout empty cart");
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public void markAsPaid() {
        if (this.status != OrderStatus.PENDING_PAYMENT) throw new DomainException("Order must be PENDING_PAYMENT");
        this.status = OrderStatus.PAID;
    }

    public void markAsShipped() {
        if (this.status != OrderStatus.PAID) throw new DomainException("Order must be PAID to ship");
        this.status = OrderStatus.SHIPPED;
    }

    public void markAsDelivered() {
        if (this.status != OrderStatus.SHIPPED) throw new DomainException("Order must be SHIPPED to be delivered");
        this.status = OrderStatus.DELIVERED_FINALIZED;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}

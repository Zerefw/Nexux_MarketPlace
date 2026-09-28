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

/**
 * Agregado raiz que representa una Orden de compra.
 */
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

    /**
     * Crea un carrito vacio.
     */
    public static Order createCart(String buyerId, String currency) {
        if (buyerId == null) throw new DomainException("El ID del comprador es obligatorio");

        return Order.builder()
                .buyerId(buyerId)
                .status(OrderStatus.CART)
                .createdAt(LocalDateTime.now())
                .totalAmount(Money.zero(currency))
                .items(new ArrayList<>())
                .build();
    }

    /**
     * Establece la direccion de envio.
     */
    public void setShippingAddress(String address) {
        this.shippingAddress = address;
    }

    /**
     * Anade un item a la orden.
     */
    public void addItem(OrderItem item) {
        if (this.status != OrderStatus.CART) throw new DomainException("Solo se pueden agregar items en estado CART");
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

    /**
     * Transiciona a pendiente de pago.
     */
    public void checkout() {
        if (this.status != OrderStatus.CART) throw new DomainException("Solo desde CART se puede hacer checkout");
        if (this.shippingAddress == null || this.shippingAddress.isBlank()) {
            throw new DomainException("La direccion de envio es obligatoria para el checkout");
        }
        if (this.items.isEmpty()) throw new DomainException("No se puede hacer checkout a un carrito vacio");
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    /**
     * Marca la orden como pagada.
     */
    public void markAsPaid() {
        if (this.status != OrderStatus.PENDING_PAYMENT) throw new DomainException("La orden debe estar en PENDING_PAYMENT");
        this.status = OrderStatus.PAID;
    }

    /**
     * Marca la orden como despachada.
     */
    public void ship() {
        if (this.status != OrderStatus.PAID) throw new DomainException("La orden debe estar en PAID para despacharse");
        this.status = OrderStatus.SHIPPED;
    }

    /**
     * Marca la orden como entregada.
     */
    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) throw new DomainException("La orden debe estar en SHIPPED para entregarse");
        this.status = OrderStatus.DELIVERED_FINALIZED;
    }
    
    /**
     * Cancela la orden.
     */
    public void cancel() {
        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED_FINALIZED) {
            throw new DomainException("No se puede cancelar en el estado actual");
        }
        this.status = OrderStatus.CANCELLED;
    }
    
    /**
     * Reembolsa la orden.
     */
    public void refund() {
        if (this.status != OrderStatus.PAID && this.status != OrderStatus.SHIPPED && this.status != OrderStatus.DELIVERED_FINALIZED) {
            throw new DomainException("La orden no esta en un estado reembolsable");
        }
        this.status = OrderStatus.REFUNDED;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}

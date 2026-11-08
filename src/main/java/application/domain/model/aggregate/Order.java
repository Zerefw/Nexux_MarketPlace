package application.domain.model.aggregate;

import application.domain.exception.DomainException;
import application.domain.model.entity.OrderItem;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.OrderStatus;
import application.domain.model.valueobject.Sku;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregado raiz que representa una Orden de compra dentro del Marketplace.
 * Gestiona el ciclo de vida completo desde el estado CART hasta su entrega o reembolso.
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
     * Crea un carrito de compras vacio para un comprador especifico.
     *
     * @param buyerId  Identificador unico del comprador.
     * @param currency Codigo ISO de la moneda para la orden.
     * @return Nueva instancia de Order en estado CART.
     */
    public static Order createCart(String buyerId, String currency) {
        if (buyerId == null || buyerId.isBlank()) {
            throw new DomainException("El ID del comprador es obligatorio");
        }
        String resolvedCurrency = (currency == null || currency.isBlank()) ? "USD" : currency;

        return Order.builder()
                .buyerId(buyerId)
                .status(OrderStatus.CART)
                .createdAt(LocalDateTime.now())
                .totalAmount(Money.zero(resolvedCurrency))
                .items(new ArrayList<>())
                .build();
    }

    /**
     * Establece la direccion fisica de envio de la orden.
     *
     * @param address Direccion de destino.
     */
    public void setShippingAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new DomainException("La direccion de envio no puede ser vacia");
        }
        this.shippingAddress = address;
    }

    /**
     * Anade una linea de producto al carrito y recalcula el monto total.
     *
     * @param item Linea de orden a agregar.
     */
    public void addItem(OrderItem item) {
        if (this.status != OrderStatus.CART) {
            throw new DomainException("Solo se pueden agregar items en estado CART");
        }
        if (item == null) {
            throw new DomainException("El item de la orden no puede ser nulo");
        }
        this.items.add(item);
        recalculateTotal();
    }

    /**
     * Elimina un item del carrito identificado por su SKU y recalcula el monto total.
     *
     * @param sku Identificador SKU del producto a remover.
     */
    public void removeItem(Sku sku) {
        if (this.status != OrderStatus.CART) {
            throw new DomainException("Solo se pueden eliminar items cuando la orden esta en estado CART");
        }
        if (sku == null) {
            throw new DomainException("El SKU a eliminar es obligatorio");
        }
        boolean removed = this.items.removeIf(item -> item.getSku().getCode().equals(sku.getCode()));
        if (!removed) {
            throw new DomainException("El SKU especificado no se encuentra en el carrito: " + sku.getCode());
        }
        recalculateTotal();
    }

    /**
     * Recalcula el monto total de la orden sumando los subtotales de cada linea.
     */
    private void recalculateTotal() {
        if (items.isEmpty()) {
            String currency = (this.totalAmount != null) ? this.totalAmount.getCurrency() : "USD";
            this.totalAmount = Money.zero(currency);
            return;
        }
        String currency = items.get(0).getUnitPrice().getCurrency();
        Money total = Money.zero(currency);
        for (OrderItem item : items) {
            total = total.add(item.getSubTotal());
        }
        this.totalAmount = total;
    }

    /**
     * Transiciona la orden del estado CART al estado PENDING_PAYMENT.
     */
    public void checkout() {
        if (this.status != OrderStatus.CART) {
            throw new DomainException("Solo desde CART se puede hacer checkout");
        }
        if (this.shippingAddress == null || this.shippingAddress.isBlank()) {
            throw new DomainException("La direccion de envio es obligatoria para el checkout");
        }
        if (this.items.isEmpty()) {
            throw new DomainException("No se puede hacer checkout a un carrito vacio");
        }
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    /**
     * Marca la orden como pagada tras la confirmacion financiera.
     */
    public void markAsPaid() {
        if (this.status != OrderStatus.PENDING_PAYMENT) {
            throw new DomainException("La orden debe estar en PENDING_PAYMENT");
        }
        this.status = OrderStatus.PAID;
    }

    /**
     * Marca la orden como despachada fisicamente hacia el comprador.
     */
    public void ship() {
        if (this.status != OrderStatus.PAID) {
            throw new DomainException("La orden debe estar en PAID para despacharse");
        }
        this.status = OrderStatus.SHIPPED;
    }

    /**
     * Marca la orden como entregada y finalizada exitosamente.
     */
    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new DomainException("La orden debe estar en SHIPPED para entregarse");
        }
        this.status = OrderStatus.DELIVERED_FINALIZED;
    }

    /**
     * Cancela una orden antes de que haya sido despachada.
     */
    public void cancel() {
        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED_FINALIZED) {
            throw new DomainException("No se puede cancelar en el estado actual");
        }
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * Marca la orden como reembolsada.
     */
    public void refund() {
        if (this.status != OrderStatus.PAID && this.status != OrderStatus.SHIPPED && this.status != OrderStatus.DELIVERED_FINALIZED) {
            throw new DomainException("La orden no esta en un estado reembolsable");
        }
        this.status = OrderStatus.REFUNDED;
    }

    /**
     * Obtiene una vista inmutable de los items de la orden.
     *
     * @return Lista inmutable de OrderItem.
     */
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}

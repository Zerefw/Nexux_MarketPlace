package application.domain.model.valueobject;

/**
 * Enumeracion que representa los posibles estados de una orden.
 */
public enum OrderStatus {
    CART,
    PENDING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED_FINALIZED,
    CANCELLED,
    REFUNDED
}

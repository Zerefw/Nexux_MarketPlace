package application.domain.services.order;

import application.domain.exception.DomainException;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.OrderItem;
import application.domain.model.entity.Product;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.Sku;
import application.domain.ports.in.command.order.AddItemToCartCommand;
import application.domain.ports.in.order.AddItemToCartUseCase;
import application.domain.ports.out.catalog.ProductRepositoryPort;
import application.domain.ports.out.order.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicacion para anadir items a un carrito existente y recalcular sus totales.
 * Implementa el caso de uso AddItemToCartUseCase.
 */
@Service
@RequiredArgsConstructor
public class AddItemToCartService implements AddItemToCartUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    /**
     * Recupera el carrito de compras, verifica que el SKU exista y este activo en el catalogo,
     * construye el nuevo item y persiste el estado actualizado de la orden.
     *
     * @param command Comando con los datos de la orden, SKU, cantidad y precio unitario.
     * @return Agregado Order actualizado.
     */
    @Override
    @Transactional
    public Order execute(AddItemToCartCommand command) {
        if (command.orderId() == null || command.orderId().isBlank()) {
            throw new DomainException("El identificador de la orden es obligatorio");
        }
        Order order = orderRepositoryPort.findById(command.orderId())
                .orElseThrow(() -> new DomainException("Orden no encontrada con ID: " + command.orderId()));

        Sku sku = new Sku(command.sku());
        Product product = productRepositoryPort.findBySku(sku)
                .orElseThrow(() -> new DomainException("Producto no encontrado en el catalogo con SKU: " + sku.getCode()));
        if (!product.isActive()) {
            throw new DomainException("No se puede agregar al carrito un producto desactivado: " + sku.getCode());
        }

        Money unitPrice = (command.unitPriceAmount() != null)
                ? new Money(command.unitPriceAmount(), command.currency() != null ? command.currency() : product.getPrice().getCurrency())
                : product.getPrice();
        OrderItem newItem = OrderItem.create(sku, command.quantity(), unitPrice);

        order.addItem(newItem);
        return orderRepositoryPort.save(order);
    }
}

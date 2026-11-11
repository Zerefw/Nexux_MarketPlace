package application.adapter.out.persistence;

import application.adapter.out.persistence.entity.OrderItemJpaEntity;
import application.adapter.out.persistence.entity.OrderJpaEntity;
import application.adapter.out.persistence.repository.OrderJpaRepository;
import application.domain.model.aggregate.Order;
import application.domain.model.entity.OrderItem;
import application.domain.model.valueobject.Money;
import application.domain.model.valueobject.Sku;
import application.domain.ports.out.order.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida de persistencia para el agregado Order.
 */
@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        List<OrderItemJpaEntity> itemEntities = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            itemEntities.add(OrderItemJpaEntity.builder()
                    .id(item.getId())
                    .sku(item.getSku().getCode())
                    .quantity(item.getQuantity())
                    .unitPriceAmount(item.getUnitPrice().getAmount())
                    .subTotalAmount(item.getSubTotal().getAmount())
                    .currency(item.getUnitPrice().getCurrency())
                    .build());
        }

        OrderJpaEntity jpa = OrderJpaEntity.builder()
                .id(order.getId())
                .buyerId(order.getBuyerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount().getAmount())
                .currency(order.getTotalAmount().getCurrency())
                .createdAt(order.getCreatedAt())
                .shippingAddress(order.getShippingAddress())
                .items(itemEntities)
                .build();

        OrderJpaEntity saved = orderJpaRepository.save(jpa);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findByBuyerId(String buyerId) {
        return orderJpaRepository.findByBuyerId(buyerId).stream()
                .map(this::toDomain)
                .toList();
    }

    private Order toDomain(OrderJpaEntity jpa) {
        List<OrderItem> domainItems = new ArrayList<>();
        if (jpa.getItems() != null) {
            for (OrderItemJpaEntity itemJpa : jpa.getItems()) {
                Money unitPrice = new Money(itemJpa.getUnitPriceAmount(), itemJpa.getCurrency());
                Money subTotal = new Money(itemJpa.getSubTotalAmount(), itemJpa.getCurrency());
                domainItems.add(OrderItem.builder()
                        .id(itemJpa.getId())
                        .sku(new Sku(itemJpa.getSku()))
                        .quantity(itemJpa.getQuantity())
                        .unitPrice(unitPrice)
                        .subTotal(subTotal)
                        .build());
            }
        }

        return Order.builder()
                .id(jpa.getId())
                .buyerId(jpa.getBuyerId())
                .status(jpa.getStatus())
                .totalAmount(new Money(jpa.getTotalAmount(), jpa.getCurrency()))
                .createdAt(jpa.getCreatedAt())
                .shippingAddress(jpa.getShippingAddress())
                .items(domainItems)
                .build();
    }
}

package com.challenge.mapper;

import com.challenge.entities.OrderEntity;
import com.challenge.entities.ProductEntity;
import com.challenge.model.Order;
import com.challenge.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OrderMapper {

    private final ProductMapper productMapper;

    public Order mapToModel(OrderEntity entity) {
        List<Product> products = entity.getProducts().stream()
                .map(productMapper::mapToProductModel)
                .collect(Collectors.toList());

        return Order.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .products(products)
                .totalAmount(entity.getTotalAmount())
                .build();
    }

    public OrderEntity mapToEntity(String idempotencyKey, Order order) {
        List<ProductEntity> products = order.getProducts().stream()
                .map(productMapper::mapToProductEntity)
                .collect(Collectors.toList());

        return OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .idempotencyKey(idempotencyKey)
                .status(order.getStatus())
                .products(products)
                .totalAmount(order.getTotalAmount())
                .build();
    }
}

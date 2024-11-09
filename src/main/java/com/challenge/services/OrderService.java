package com.challenge.services;

import com.challenge.entities.OrderEntity;
import com.challenge.enums.StatusEnum;
import com.challenge.mapper.OrderMapper;
import com.challenge.model.Order;
import com.challenge.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderEntity saveOrder(String idempotencyKey, Order order) {

        Optional<OrderEntity> existingOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
        if (existingOrder.isPresent()) {
            return existingOrder.get();
        }

        OrderEntity orderEntity = Optional.ofNullable(order)
                .map(ord -> orderMapper.mapToEntity(idempotencyKey, ord)).get();

        BigDecimal total = orderEntity.getProducts().stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderEntity.setTotalAmount(total);
        orderEntity.setStatus(StatusEnum.CALCULATED);
        return orderRepository.save(orderEntity);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id).map(orderMapper::mapToModel);
    }
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::mapToModel);
    }

    public void deleteAll() {
        orderRepository.deleteAll();
    }
}


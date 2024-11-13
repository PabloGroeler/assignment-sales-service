package com.challenge.services;

import com.challenge.entities.OrderEntity;
import com.challenge.enums.StatusEnum;
import com.challenge.mapper.OrderMapper;
import com.challenge.message.producer.KafkaProducer;
import com.challenge.model.Order;
import com.challenge.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaProducer producerService;

    public OrderEntity sendOrder(String idempotencyKey, Order order) {
        log.info("Starting sendOrder with ID: {}", order.getId());

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

        producerService.sendMessage(orderEntity);
        log.info("Starting sendOrder with ID: {} is done", order.getId());
        return orderEntity;
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id).map(orderMapper::mapToModel);
    }
    public Page<Order> getAllOrders(Pageable pageable) {
        log.info("Getting all orders");
        return orderRepository.findAll(pageable)
                .map(orderMapper::mapToModel);
    }

    public void deleteAll() {
        orderRepository.deleteAll();
    }
}


package com.challenge.service;


import com.challenge.entities.OrderEntity;
import com.challenge.entities.ProductEntity;
import com.challenge.enums.StatusEnum;
import com.challenge.mapper.OrderMapper;
import com.challenge.model.Order;
import com.challenge.repositories.OrderRepository;
import com.challenge.message.producer.KafkaProducer;
import com.challenge.services.OrderService;
import com.challenge.utils.ServiceTestResources;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class OrderServiceTest extends ServiceTestResources {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;
    @Mock
    private KafkaProducer producerService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testSendOrder() {
        Order order = Order.builder().build();
        ProductEntity product1 = ProductEntity.builder().name("Product1").price(BigDecimal.valueOf(10)).quantity(2).build();
        ProductEntity product2 = ProductEntity.builder().name("Product2").price(BigDecimal.valueOf(20)).quantity(2).build();
        OrderEntity orderEntity = OrderEntity.builder().build();
        orderEntity.setProducts(List.of(product1, product2));

        when(orderMapper.mapToEntity("idempotencyKey", order)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        OrderEntity savedOrder = orderService.sendOrder("idempotencyKey", order);

        assertEquals(BigDecimal.valueOf(60), orderEntity.getTotalAmount());
        assertEquals(StatusEnum.CALCULATED, orderEntity.getStatus());
    }

    @Test
    void testGetOrderById() {
        String orderId = "order123";
        OrderEntity orderEntity = OrderEntity.builder().build();
        Order order = Order.builder().build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.mapToModel(orderEntity)).thenReturn(order);

        Optional<Order> result = orderService.getOrderById(orderId);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        Mockito.verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetAllOrders() {
        OrderEntity orderEntity1 = OrderEntity.builder().build();
        OrderEntity orderEntity2 = OrderEntity.builder().build();
        Order order1 = Order.builder().build();
        Order order2 = Order.builder().build();
        PageRequest pageRequest = PageRequest.of(1, 2);
        List<OrderEntity> orders = List.of(orderEntity1, orderEntity2);

        Page<OrderEntity> orderPage = new PageImpl<>(orders, pageRequest, orders.size());

        when(orderRepository.findAll(pageRequest)).thenReturn(orderPage);
        when(orderMapper.mapToModel(orderEntity1)).thenReturn(order1);
        when(orderMapper.mapToModel(orderEntity2)).thenReturn(order2);

        Page<Order> result = orderService.getAllOrders(pageRequest);

        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(order1));
        assertTrue(result.getContent().contains(order2));
        Mockito.verify(orderRepository, times(1)).findAll(pageRequest);
    }
}

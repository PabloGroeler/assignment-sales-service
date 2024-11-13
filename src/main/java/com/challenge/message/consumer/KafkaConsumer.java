package com.challenge.message.consumer;

import com.challenge.entities.OrderEntity;
import com.challenge.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaConsumer {

    private final OrderRepository repository;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void receive(@Payload OrderEntity message) {
        log.info("Kafka message received: {}", message);
        repository.save(message);
    }
}

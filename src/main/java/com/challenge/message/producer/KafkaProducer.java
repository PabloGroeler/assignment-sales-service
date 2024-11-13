package com.challenge.message.producer;

import com.challenge.entities.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@EnableKafka
@Component
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, OrderEntity> kafkaTemplate;
    private final String topicName;
    public KafkaProducer(KafkaTemplate<String, OrderEntity> kafkaTemplate,
                         @Value("${spring.kafka.template.default-topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(OrderEntity orderEntity) {
        log.info("Sending message on producer with ID: {}", orderEntity.getId());
        kafkaTemplate.send(topicName, orderEntity);
    }
}

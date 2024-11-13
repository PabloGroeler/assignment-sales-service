package com.challenge.message.consumer;

import com.challenge.entities.OrderEntity;
import com.challenge.enums.StatusEnum;
import com.challenge.message.KafkaConfig;
import com.challenge.repositories.OrderRepository;
import com.challenge.utils.ControllerTestResources;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@EnableKafka
@EmbeddedKafka(partitions = 1, topics = "assignment-topic")
@Import(KafkaConfig.class)
class KafkaConsumerTest extends ControllerTestResources {

    @InjectMocks
    private KafkaConsumer consumer;
    @MockBean
    private OrderRepository repository;
    @Autowired
    private KafkaTemplate<String, OrderEntity> kafkaTemplate;

    private String topicName = "assignment-topic";

    @Test
    void testKafkaListener() throws InterruptedException {
        OrderEntity entity = OrderEntity.builder()
                .id("id")
                .idempotencyKey("idempotencyKey")
                .status(StatusEnum.CALCULATED)
                .build();

        kafkaTemplate.send(topicName, entity);
        Thread.sleep(2000);
        verify(repository, times(1)).save(any(OrderEntity.class));

    }

}
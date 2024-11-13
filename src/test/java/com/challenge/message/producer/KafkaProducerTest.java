package com.challenge.message.producer;

import com.challenge.entities.OrderEntity;
import com.challenge.utils.ServiceTestResources;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class KafkaProducerTest extends ServiceTestResources {

    @InjectMocks
    private KafkaProducer producer;

    @Mock
    private KafkaTemplate<String, OrderEntity> kafkaTemplate;

    @Test
    void sendMessage_ShouldSendOrderEntityMessage() {
        ReflectionTestUtils.setField(producer, "topicName", "topic");

        OrderEntity orderEntity = OrderEntity.builder().build();
        producer.sendMessage(orderEntity);

        verify(kafkaTemplate, times(1)).send(eq("topic"), eq(orderEntity));

    }

}
package com.synpulse8.ebank.Configuration;

import com.synpulse8.ebank.DTO.TransactionDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class TransactionKafkaConfig {
    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name("transaction").partitions(1).replicas(1).build();
    }

    @Bean
    public KafkaTemplate<UUID, TransactionDTO> transactionKafkaTemplate() {
        return new KafkaTemplate<>(transactionProducerFactory());
    }

    @Bean
    public ProducerFactory<UUID, TransactionDTO> transactionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // Add Kafka producer properties
        configProps.put("bootstrap.servers", "localhost:9092");
        configProps.put("key.serializer", UUIDSerializer.class);
        configProps.put("value.serializer", JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionDTO> transactionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());
        return factory;
    }

    // Create the ConsumerFactory bean
    @Bean
    public ConsumerFactory<String, TransactionDTO> transactionConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // Add Kafka consumer properties
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
                new JsonDeserializer<>(TransactionDTO.class, false));
    }


}

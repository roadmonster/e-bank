package com.synpulse8.ebank.Configuration;

import com.synpulse8.ebank.DTO.BalanceUpdateDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
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
public class AccountBalanceKafkaConfig {
    @Bean
    public NewTopic accBalanceTopic() {
        return TopicBuilder.name("account_balance").partitions(1).replicas(1).build();
    }

    // Create the KafkaTemplate bean
    @Bean
    public KafkaTemplate<UUID, BalanceUpdateDTO> accBalanceKafkaTemplate() {
        return new KafkaTemplate<>(accBalanceProducerFactory());
    }


    // Create the ProducerFactory bean
    @Bean
    public ProducerFactory<UUID, BalanceUpdateDTO> accBalanceProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // Add Kafka producer properties
        configProps.put("bootstrap.servers", "localhost:9092");
        configProps.put("key.serializer", StringSerializer.class);
        configProps.put("value.serializer", JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, BalanceUpdateDTO> accBalanceKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<UUID, BalanceUpdateDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accBalanceConsumerFactory());
        return factory;
    }

    // Create the ConsumerFactory bean
    @Bean
    public ConsumerFactory<UUID, BalanceUpdateDTO> accBalanceConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // Add Kafka consumer properties
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(configProps, new UUIDDeserializer(),
                new JsonDeserializer<>(BalanceUpdateDTO.class, false));
    }
}

package com.synpulse8.ebank.Configuration;

import com.synpulse8.ebank.DTO.TransactionDTO;
import com.synpulse8.ebank.Utilities.ConsumerConfigPropGenerator;
import com.synpulse8.ebank.Utilities.ProducerConfigPropGenerator;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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
        return new DefaultKafkaProducerFactory<>(ProducerConfigPropGenerator.generateConfig());
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
        return new DefaultKafkaConsumerFactory<>(ConsumerConfigPropGenerator.getConsumerConfigProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(TransactionDTO.class, false));
    }


}

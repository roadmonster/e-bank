package com.synpulse8.ebank.Configuration;

import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
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
public class AccountBalanceKafkaConfig {
    @Bean
    public NewTopic accBalanceTopic() {
        return TopicBuilder.name("account_balance").partitions(1).replicas(1).build();
    }

    // Create the KafkaTemplate bean
    @Bean
    public KafkaTemplate<String, BalanceUpdateRequest> accBalanceKafkaTemplate() {
        return new KafkaTemplate<>(accBalanceProducerFactory());
    }


    // Create the ProducerFactory bean
    @Bean
    public ProducerFactory<String, BalanceUpdateRequest> accBalanceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(ProducerConfigPropGenerator.generateConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BalanceUpdateRequest> accBalanceKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BalanceUpdateRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accBalanceConsumerFactory());
        return factory;
    }

    // Create the ConsumerFactory bean
    @Bean
    public ConsumerFactory<String, BalanceUpdateRequest> accBalanceConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(ConsumerConfigPropGenerator.getConsumerConfigProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(BalanceUpdateRequest.class, false));
    }
}

package com.synpulse8.ebank.Configuration;

import com.synpulse8.ebank.DTO.AccountCreationDTO;
import com.synpulse8.ebank.Utilities.ConsumerConfigPropGenerator;
import com.synpulse8.ebank.Utilities.ProducerConfigPropGenerator;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

@Configuration
@EnableKafka
public class AccountKafkaConfig {

    // Create the topics
    @Bean
    public NewTopic accountTopic() {
        return TopicBuilder.name("account_creation").partitions(1).replicas(1).build();
    }

    // Create the KafkaTemplate bean

    @Bean
    public KafkaTemplate<String, AccountCreationDTO> accountKafkaTemplate() {
        return new KafkaTemplate<>(accountProducerFactory());
    }

    // Create the ProducerFactory bean
    @Bean
    public ProducerFactory<String, AccountCreationDTO> accountProducerFactory() {
        return new DefaultKafkaProducerFactory<>(ProducerConfigPropGenerator.generateConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountCreationDTO> accountKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountCreationDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountConsumerFactory());
        return factory;
    }

    // Create the ConsumerFactory bean
    @Bean
    public ConsumerFactory<String, AccountCreationDTO> accountConsumerFactory() {


        return new DefaultKafkaConsumerFactory<>(ConsumerConfigPropGenerator.getConsumerConfigProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(AccountCreationDTO.class, false));
    }

}

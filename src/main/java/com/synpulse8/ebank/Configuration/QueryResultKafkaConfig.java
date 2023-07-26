package com.synpulse8.ebank.Configuration;

import com.synpulse8.ebank.DTO.QueryResponse;
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

@Configuration
@EnableKafka
public class QueryResultKafkaConfig {
    @Bean
    public NewTopic queryResponseTopic() {
        return TopicBuilder.name("Query_Result").partitions(1).replicas(1).build();
    }

    // Create the KafkaTemplate bean
    @Bean
    public KafkaTemplate<String, QueryResponse> queryResponseKafkaTemplate() {
        return new KafkaTemplate<>(queryResponseProducerFactory());
    }

    // Create the ProducerFactory bean
    @Bean
    public ProducerFactory<String, QueryResponse> queryResponseProducerFactory() {
        return new DefaultKafkaProducerFactory<>(ProducerConfigPropGenerator.generateConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, QueryResponse> queryResponseKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String,QueryResponse> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(queryResponseConsumerFactory());
        return factory;
    }

    // Create the ConsumerFactory bean
    @Bean
    public ConsumerFactory<String, QueryResponse> queryResponseConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(ConsumerConfigPropGenerator.getConsumerConfigProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(QueryResponse.class, false));
    }
}

package com.synpulse8.ebank.Configuration;

import com.synpulse8.ebank.DTO.QueryRequest;
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
public class QueryRequestKafkaConfig {
    // Create the topics
    @Bean
    public NewTopic queryRequestTopic() {
        return TopicBuilder.name("Query_Request").partitions(1).replicas(1).build();
    }

    // Create the KafkaTemplate bean
    @Bean
    public KafkaTemplate<String, QueryRequest> queryKafkaTemplate() {
        return new KafkaTemplate<>(queryProducerFactory());
    }

    // Create the ProducerFactory bean
    @Bean
    public ProducerFactory<String, QueryRequest> queryProducerFactory() {
        return new DefaultKafkaProducerFactory<>(ProducerConfigPropGenerator.generateConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, QueryRequest> queryRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, QueryRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(queryRequestConsumerFactory());
        return factory;
    }

    // Create the ConsumerFactory bean
    @Bean
    public ConsumerFactory<String, QueryRequest> queryRequestConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(ConsumerConfigPropGenerator.getConsumerConfigProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(QueryRequest.class, false));
    }
}

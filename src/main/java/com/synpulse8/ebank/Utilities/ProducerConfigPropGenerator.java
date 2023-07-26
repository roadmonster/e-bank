package com.synpulse8.ebank.Utilities;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class ProducerConfigPropGenerator {
     public static Map<String, Object> generateConfig() {
         Map<String, Object> configProps = new HashMap<>();
         // Add Kafka producer properties
         configProps.put("bootstrap.servers", "localhost:9092");
         configProps.put("key.serializer", StringSerializer.class);
         configProps.put("value.serializer", JsonSerializer.class);
         return configProps;
     }
}

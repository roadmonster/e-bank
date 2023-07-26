package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.QueryResponse;
import com.synpulse8.ebank.Utilities.KafkaResponseProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryResponseConsumer {

    private final KafkaResponseProcessor responseProcessor;

    @Autowired
    public QueryResponseConsumer(KafkaResponseProcessor responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    // HTTP endpoint for polling
    @GetMapping("/pollResponse")
    public Object pollResponse(@RequestParam String requestId) {
        // Use CompletableFuture or BlockingQueue to manage pending requests and responses
        return responseProcessor.pollResponse(requestId);
    }

    // Kafka listener to handle responses from Kafka
    @KafkaListener(topics = "Query_Result", groupId = "query-response-consumer",
            containerFactory = "queryResponseKafkaListenerContainerFactory")
    public void handleKafkaResponse(QueryResponse response) {
        // Process the response and add it to the CompletableFuture or BlockingQueue
        responseProcessor.processResponse(response);
    }
}


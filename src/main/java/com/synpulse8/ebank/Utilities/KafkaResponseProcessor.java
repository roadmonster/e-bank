package com.synpulse8.ebank.Utilities;

import com.synpulse8.ebank.DTO.QueryResponse;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaResponseProcessor {
    // Map to store CompletableFuture for pending requests and responses
    private final Map<String, CompletableFuture<QueryResponse>> responseMap = new ConcurrentHashMap<>();

    // Method to initiate the request and store the CompletableFuture
    public CompletableFuture<QueryResponse> initiateRequest(String requestId) {
        CompletableFuture<QueryResponse> completableFuture = new CompletableFuture<>();
        responseMap.put(requestId, completableFuture);
        return completableFuture;
    }

    // Method to process the Kafka response and complete the corresponding CompletableFuture
    public void processResponse(QueryResponse response) {
        CompletableFuture<QueryResponse> completableFuture = responseMap.get(response.getRequestId());
        if (completableFuture != null) {
            completableFuture.complete(response);
        }
    }

    // Method to poll for response based on requestId
    public QueryResponse pollResponse(String requestId) {
        CompletableFuture<QueryResponse> completableFuture = responseMap.get(requestId);
        if (completableFuture != null && completableFuture.isDone()) {
            // Use CompletableFuture.getNow() to get the response without waiting if it's already completed
            return completableFuture.getNow(null);
        } else {
            // The response is not available yet
            return null;
        }
    }
}


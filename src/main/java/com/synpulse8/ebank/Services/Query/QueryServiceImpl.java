package com.synpulse8.ebank.Services.Query;

import com.synpulse8.ebank.DTO.QueryRequest;
import com.synpulse8.ebank.DTO.QueryResponse;
import com.synpulse8.ebank.DTO.TransactionDto;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Utilities.KafkaResponseProcessor;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class QueryServiceImpl implements QueryService{

    private final KafkaResponseProcessor responseProcessor;
    private final KafkaTemplate<String, QueryRequest> queryRequestDTOKafkaTemplate;

    @Override
    public List<TransactionDto> queryTransactions(Date from, Date to, Long userId) {
        String requestId = UUID.randomUUID().toString();

        // Initiate the request and get the CompletableFuture associated with the requestId
        CompletableFuture<QueryResponse> completableFuture = responseProcessor.initiateRequest(requestId);

        // build the query request dto
        QueryRequest dto = QueryRequest.builder()
                .requestId(requestId)
                .from(from).to(to).userId(userId).build();

        // Publish the query transaction request to the Kafka request channel
        queryRequestDTOKafkaTemplate.send("Query_Request", requestId, dto);

        // Wait for the response from the CompletableFuture
        try {
            QueryResponse response = completableFuture.get();
            // Process and return the response
            return processResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions and errors
            return null;
        }
    }

    private List<TransactionDto> processResponse(QueryResponse response) {
        return response.getTransactions();
    }
}

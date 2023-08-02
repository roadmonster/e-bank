package com.synpulse8.ebank;

import com.synpulse8.ebank.Consumer.QueryResponseConsumer;
import com.synpulse8.ebank.DTO.QueryResponse;
import com.synpulse8.ebank.DTO.TransactionDto;
import com.synpulse8.ebank.Utilities.KafkaResponseProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class QueryResponseConsumerTest {

    @Autowired
    private QueryResponseConsumer queryResponseConsumer;

    @MockBean
    private KafkaResponseProcessor responseProcessor;

    @Test
    void pollResponse_ShouldReturnResponseFromProcessor() {
        // Arrange
        String requestId = "53f17898-2696-4d9e-9e0c-ae02a5a56d0e";
        QueryResponse response = QueryResponse.builder()
                .requestId(UUID.randomUUID().toString())
                .transactions(new ArrayList<TransactionDto>())
                .build();

        // Set up the behavior of responseProcessor.pollResponse
        when(responseProcessor.pollResponse(requestId)).thenReturn(response);

        // Act
        Object result = queryResponseConsumer.pollResponse(requestId);

        // Assert
        assertEquals(response, result);
    }

    @Test
    void handleKafkaResponse_ShouldProcessResponseAndAddToProcessor() {
        // Arrange
        QueryResponse response = new QueryResponse();
        // Set the necessary properties for response

        // Act
        queryResponseConsumer.handleKafkaResponse(response);

        // Assert
        // Verify that responseProcessor.processResponse was called with the correct QueryResponse object
        verify(responseProcessor).processResponse(response);
    }
}


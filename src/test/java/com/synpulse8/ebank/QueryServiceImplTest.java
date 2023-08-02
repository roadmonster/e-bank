package com.synpulse8.ebank;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.synpulse8.ebank.DTO.QueryRequest;
import com.synpulse8.ebank.DTO.QueryResponse;
import com.synpulse8.ebank.DTO.TransactionDto;
import com.synpulse8.ebank.Enums.Currency;
import com.synpulse8.ebank.Services.Query.QueryServiceImpl;
import com.synpulse8.ebank.Utilities.KafkaResponseProcessor;
import com.synpulse8.ebank.Utilities.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
public class QueryServiceImplTest {

    @Mock
    private KafkaResponseProcessor responseProcessor;

    @Mock
    private KafkaTemplate<String, QueryRequest> queryRequestDTOKafkaTemplate;

    private QueryServiceImpl queryService;

    @BeforeEach
    public void setUp() {
        queryService = new QueryServiceImpl(responseProcessor,queryRequestDTOKafkaTemplate);
    }


    @Test
    void testQueryTransactions_ValidRequest_SuccessfulResponse() {
        // Arrange
        String requestId = "12345"; // Predefined request ID for simplicity
        Date from = new Date();// Predefined 'from' date;
        Date to = new Date();// Predefined 'to' date;
        Long userId = 10L;// Predefined user ID;

        List<TransactionDto> mockTransactions = new ArrayList<>();
        TransactionDto t1 = TransactionDto.builder()
                .transactionId(UUIDGenerator.generateUUID())
                .amount(BigDecimal.valueOf(100L))
                .currency(Currency.USD)
                .accountId(10L)
                .transactionTime(new Timestamp(System.currentTimeMillis()))
                .build();

        mockTransactions.add(t1);

        QueryResponse mockQueryResponse = new QueryResponse(requestId, mockTransactions);

        // Mock the responseProcessor
        CompletableFuture<QueryResponse> completableFuture = CompletableFuture.completedFuture(mockQueryResponse);
        when(responseProcessor.initiateRequest(anyString())).thenReturn(completableFuture);


        // Act
        List<TransactionDto> result = queryService.queryTransactions(from, to, userId);

        // Assert
        // Validate the result based on the mocked response
        List<TransactionDto> expected = mockTransactions;
        assertEquals(expected, result);
    }

}


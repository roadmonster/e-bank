package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.QueryRequestDTO;
import com.synpulse8.ebank.DTO.QueryResponse;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@EnableKafka
@AllArgsConstructor
public class QueryReqConsumer {
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, QueryResponse> queryResponseKafkaTemplate;
    @KafkaListener(topics = "Query_Request", groupId = "query-group",
            containerFactory = "queryRequestKafkaListenerContainerFactory")
    public void handleQuery(QueryRequestDTO dto) {
        Date from = dto.getFrom(), to = dto.getTo();
        String requestId = dto.getRequestId();
        Long userId = dto.getUserId();
        List<Transaction> transactions = null;
        if (from == null && to == null) {
            transactions = transactionRepository.findAllByUserid(userId);
        } else {
            transactions = transactionRepository.findTransactionByUserIdDuringDate(userId, from, to);
        }

        QueryResponse response = new QueryResponse(requestId, transactions);
        queryResponseKafkaTemplate.send("Query_Result", response);
    }
}

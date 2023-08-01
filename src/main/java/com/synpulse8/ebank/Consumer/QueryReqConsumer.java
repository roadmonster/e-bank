package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.QueryRequest;
import com.synpulse8.ebank.DTO.QueryResponse;
import com.synpulse8.ebank.DTO.TransactionDto;
import com.synpulse8.ebank.Mapper.TransactionMapper;
import com.synpulse8.ebank.Repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableKafka
@AllArgsConstructor
public class QueryReqConsumer {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final KafkaTemplate<String, QueryResponse> queryResponseKafkaTemplate;
    @KafkaListener(topics = "Query_Request", groupId = "query-group",
            containerFactory = "queryRequestKafkaListenerContainerFactory")
    public void handleQuery(QueryRequest dto) {
        Date from = dto.getFrom(), to = dto.getTo();
        String requestId = dto.getRequestId();
        Long userId = dto.getUserId();
        List<TransactionDto> result;
        if (from == null && to == null) {

            result = transactionRepository.findAllByUserid(userId).stream()
                    .map(transactionMapper::mapToTransactionDto)
                    .collect(Collectors.toList());
        } else {
            result = transactionRepository.findTransactionByUserIdDuringDate(userId, from, to)
            .stream().map(transactionMapper::mapToTransactionDto)
            .collect(Collectors.toList());
        }

        QueryResponse response = new QueryResponse(requestId, result);
        queryResponseKafkaTemplate.send("Query_Result", response);
    }
}

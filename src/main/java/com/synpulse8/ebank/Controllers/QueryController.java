package com.synpulse8.ebank.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synpulse8.ebank.Models.Transaction;
import com.synpulse8.ebank.Services.QueryService;
import com.synpulse8.ebank.Utilities.DateBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class QueryController {
    private final QueryService queryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/query/user/{userId}")
    public ResponseEntity<String> queryTransactionByUserId(@PathVariable Long userId) throws JsonProcessingException {
        List<Transaction> result = queryService.queryTransactions(null, null, userId);
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(result));

    }

    @GetMapping("/query/check-period/{userId}")
    public ResponseEntity<String> queryTransactionByUserBetweenDates(@PathVariable Long userId,
                                                                     @RequestParam int fromYear,
                                                                     @RequestParam int fromMonth,
                                                                     @RequestParam int fromDay,
                                                                     @RequestParam int toYear,
                                                                     @RequestParam int toMonth,
                                                                     @RequestParam int toDay)
            throws JsonProcessingException {
        Date fromDate = DateBuilder.formDate(fromYear, fromMonth, fromDay);
        Date toDate = DateBuilder.formDate(toYear, toMonth, toDay);
        List<Transaction> result = queryService.queryTransactions(fromDate, toDate, userId);
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(result));
    }
}

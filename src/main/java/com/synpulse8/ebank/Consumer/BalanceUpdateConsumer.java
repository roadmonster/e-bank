package com.synpulse8.ebank.Consumer;

import com.synpulse8.ebank.DTO.BalanceUpdateRequest;
import com.synpulse8.ebank.Services.Account.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@AllArgsConstructor
public class BalanceUpdateConsumer {

    // TO DO will need to refactor this
    private final AccountService accountService;
    @KafkaListener(topics = "account_balance", groupId = "balance-group",
            containerFactory = "accBalanceKafkaListenerContainerFactory")
    public void handleBalanceUpdateEvent(BalanceUpdateRequest dto) {
        accountService.updateBalance(dto);
    }

}

package com.synpulse8.ebank.Mapper;
import com.synpulse8.ebank.DTO.TransactionDto;
import com.synpulse8.ebank.Models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "accountId", expression = "java(transaction.getAccount().getId())")
    TransactionDto mapToTransactionDto(Transaction transaction);

    // Dummy method to get the map struct working, will not
    Transaction mapToTransaction(TransactionDto transactionDto);
}



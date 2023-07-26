package com.synpulse8.ebank.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class QueryRequestDTO {
    Date from;
    Date to;
    Long userId;
    String requestId;
}

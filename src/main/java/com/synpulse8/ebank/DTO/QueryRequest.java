package com.synpulse8.ebank.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryRequest {
    Date from;
    Date to;
    Long userId;
    String requestId;
}

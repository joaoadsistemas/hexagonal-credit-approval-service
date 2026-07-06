package com.joao.hexagonal_credit_approval_service.adapter.out.message;

import com.joao.hexagonal_credit_approval_service.application.core.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditAnalysisResultMessage {
    private UUID analysisId;
    private UUID customerId;
    private Status status;
    private BigDecimal approvedLimit = BigDecimal.ZERO;
}


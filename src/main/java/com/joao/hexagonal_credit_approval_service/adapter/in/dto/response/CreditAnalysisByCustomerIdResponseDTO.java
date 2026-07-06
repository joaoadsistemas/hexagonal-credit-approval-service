package com.joao.hexagonal_credit_approval_service.adapter.in.dto.response;

import com.joao.hexagonal_credit_approval_service.application.core.domain.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreditAnalysisByCustomerIdResponseDTO {
    private UUID analysisId;
    private Status status;
    private String denialReason;
    private LocalDateTime analysisDate;
    private BigDecimal approvedLimit = BigDecimal.ZERO;
}
